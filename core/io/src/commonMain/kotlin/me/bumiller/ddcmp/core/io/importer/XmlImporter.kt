package me.bumiller.ddcmp.core.io.importer

import kotlinx.serialization.ExperimentalSerializationApi
import me.bumiller.ddcmp.core.io.model.Xml
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.model.io.ImportResult
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import net.devrieze.xmlutil.serialization.kxio.decodeFromSource
import nl.adaptivity.xmlutil.serialization.XML
import no.synth.kmpzip.zip.ZipFile
import kotlin.time.Clock
import kotlin.time.Instant

private const val DREAMS_FILE_NAME = "dreams"
private const val PERSONS_FILE_NAME = "persons"
private const val EMOTIONS_FILE_NAME = "emotions"

internal class XmlImporter(
    private val saveDream: SaveDreamUseCase,
    private val savePerson: SavePersonUseCase,
    private val saveEmotion: SaveEmotionUseCase
) : ZipFileImporter() {

    override suspend fun importBackup(zipFile: ZipFile): ImportResult {
        val now = Clock.System.now()
        val fileFormatError = ImportResult.Failure.FileFormat(0, now)

        //
        // Reading the data
        //

        val dreams = zipFile.decode<Xml.DreamsRoot>(DREAMS_FILE_NAME)?.dreams ?: return fileFormatError
        val emotions = zipFile.decode<Xml.EmotionsRoot>(EMOTIONS_FILE_NAME)?.emotions ?: return fileFormatError
        val persons = zipFile.decode<Xml.PersonsRoot>(PERSONS_FILE_NAME)?.persons ?: return fileFormatError

        //
        // Verifying the data
        //

        val emotionIdsValid = dreams
            .flatMap { dream -> dream.emotions.map { it.ref } }
            .distinct()
            .all { emotionId -> emotions.any { it.id == emotionId } }
        val personIdsValid = dreams
            .flatMap { dream -> dream.persons.map { it.ref } }
            .distinct()
            .all { personId -> persons.any { it.id == personId } }
        val confidsValid = dreams
            .map { it.confidentiality }
            .distinct()
            .all { confid -> Confidentiality.entries.any { it.name == confid } }

        if(!emotionIdsValid || !personIdsValid || !confidsValid)
            return ImportResult.Failure.DataConsistency(0, now)

        //
        // Saving the data
        //

        persons.forEach { person ->
            savePerson(person.toModel())
        }
        emotions.forEach { emotion ->
            saveEmotion(emotion.toModel())
        }

        dreams.forEach { dream ->
            val personsForDream = dream
                .persons
                .map { it.ref }
                .mapNotNull { personId -> persons.find { it.id == personId } }
                .map { it.toModel() }
            val emotionsForDream = dream
                .emotions
                .map { it.ref }
                .mapNotNull { emotionId -> emotions.find { it.id == emotionId } }
                .map { it.toModel() }

            val input = SaveDreamUseCase.I(dream.toModel(), personsForDream, emotionsForDream)
            saveDream(input)
        }

        return ImportResult.Success.Normal(
            emotionCount = emotions.size,
            dreamCount = dreams.size,
            personCount = persons.size,
            firstDream = dreams.minOf { it.dreamt }.let(Instant::fromEpochMilliseconds),
            lastDream = dreams.maxOf { it.dreamt }.let(Instant::fromEpochMilliseconds),
            id = 0,
            timestamp = now
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    private inline fun <reified T> ZipFile.decode(name: String): T? {
        val source = sourceForName(name, IOFileType.Xml) ?: return null

        return XML.v1.decodeFromSource(source)
    }

}