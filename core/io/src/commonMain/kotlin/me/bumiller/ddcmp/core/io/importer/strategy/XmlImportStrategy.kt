package me.bumiller.ddcmp.core.io.importer.strategy

import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import me.bumiller.ddcmp.common.captureException
import me.bumiller.ddcmp.core.io.common.sourceForName
import me.bumiller.ddcmp.core.io.importer.ZipFileImporter.DataImportStrategy
import me.bumiller.ddcmp.core.io.model.Xml
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.SaveTagUseCase
import net.devrieze.xmlutil.serialization.kxio.decodeFromSource
import nl.adaptivity.xmlutil.serialization.XML
import no.synth.kmpzip.zip.ZipFile
import kotlin.time.Instant

private const val DREAMS_FILE_NAME = "dreams"
private const val PERSONS_FILE_NAME = "persons"
private const val EMOTIONS_FILE_NAME = "emotions"
private const val TAGS_FILE_NAME = "tags"

internal class XmlImportStrategy(
    private val saveDream: SaveDreamUseCase,
    private val savePerson: SavePersonUseCase,
    private val saveEmotion: SaveEmotionUseCase,
    private val saveTag: SaveTagUseCase
) : DataImportStrategy {

    override suspend fun readDataFrom(zipFile: ZipFile): DataImportStrategy.DataResult {
        try {
            //
            // Reading the data
            //

            val dreams = zipFile.decode<Xml.DreamsRoot>(DREAMS_FILE_NAME).dreams
            val emotions = zipFile.decode<Xml.EmotionsRoot>(EMOTIONS_FILE_NAME).emotions
            val persons = zipFile.decode<Xml.PersonsRoot>(PERSONS_FILE_NAME).persons
            val tags = zipFile.decode<Xml.TagsRoot>(TAGS_FILE_NAME).tags

            //
            // Verifying the data
            //

            val emotionIds = dreams
                .flatMap { dream -> dream.emotions.map { it.ref } }
            val personIds = dreams
                .flatMap { dream -> dream.persons.map { it.ref } }
            val tagIds = dreams
                .flatMap { dream -> dream.tags.map { it.ref } } union
                    persons.flatMap { person -> person.tags.map { it.ref } }

            val emotionIdsValid = emotionIds
                .distinct()
                .all { emotionId -> emotions.any { it.id == emotionId } }
            val personIdsValid = personIds
                .distinct()
                .all { personId -> persons.any { it.id == personId } }
            val tagIdsValid = tagIds
                .distinct()
                .all { tagId -> tags.any { it.id == tagId } }

            val confidsValid = dreams
                .map { it.confidentiality }
                .distinct()
                .all { confid -> Confidentiality.entries.any { it.name == confid } }

            val integrityViolated =
                !emotionIdsValid || !personIdsValid || !confidsValid || !tagIdsValid
            if (integrityViolated) return DataImportStrategy.DataResult.DataIntegrity

            //
            // Saving the data
            //

            tags.forEach { tag ->
                val input = SaveTagUseCase.I(tag.toModel())
                saveTag(input)
            }
            persons.forEach { person ->
                val input = SavePersonUseCase.I(
                    person = person.toModel(),
                    tags = person.tags
                        .map { it.ref }
                        .map { tags.find { tag -> tag.id == it }!! }
                        .map { it.toModel() }
                )
                savePerson(input)
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
                val tagsForDream = dream
                    .tags
                    .map { it.ref }
                    .mapNotNull { tagId -> tags.find { it.id == tagId } }
                    .map { it.toModel() }

                val input = SaveDreamUseCase.I(
                    dream.toModel(),
                    personsForDream,
                    emotionsForDream,
                    tagsForDream
                )
                saveDream(input)
            }

            return DataImportStrategy.DataResult.Success(
                emotionCount = emotions.size,
                dreamCount = dreams.size,
                personCount = persons.size,
                firstDream = dreams.minOf { it.dreamt }.let(Instant::fromEpochMilliseconds),
                lastDream = dreams.maxOf { it.dreamt }.let(Instant::fromEpochMilliseconds)
            )
        } catch (e: IOException) {
            captureException(e)
            return DataImportStrategy.DataResult.ReadError
        } catch (e: SerializationException) {
            captureException(e)
            return DataImportStrategy.DataResult.DataFormat
        }
    }

    private inline fun <reified T> ZipFile.decode(name: String): T {
        val source = sourceForName(name, IOFileType.Xml)
            ?: throw IllegalArgumentException("No zip entry found for name $name")

        return XML.v1.decodeFromSource(source)
    }

}