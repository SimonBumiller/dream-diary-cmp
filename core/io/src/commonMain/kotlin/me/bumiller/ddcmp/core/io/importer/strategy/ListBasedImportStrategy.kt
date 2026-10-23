package me.bumiller.ddcmp.core.io.importer.strategy

import kotlinx.io.IOException
import kotlinx.io.readByteArray
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.decodeFromSource
import me.bumiller.ddcmp.common.captureException
import me.bumiller.ddcmp.core.io.common.sourceForName
import me.bumiller.ddcmp.core.io.importer.ZipFileImporter.DataImportStrategy
import me.bumiller.ddcmp.core.io.importer.ZipFileImporter.DataImportStrategy.DataResult
import me.bumiller.ddcmp.core.io.model.ListBased
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.SaveTagUseCase
import net.mamoe.yamlkt.Yaml
import no.synth.kmpzip.zip.ZipFile
import kotlin.time.Instant

internal class ListBasedImportStrategy(
    private val saveDream: SaveDreamUseCase,
    private val savePerson: SavePersonUseCase,
    private val saveEmotion: SaveEmotionUseCase,
    private val saveTag: SaveTagUseCase,
    private val fileType: IOFileType
) : DataImportStrategy {

    init {
        require(fileType in setOf(IOFileType.Yaml, IOFileType.Json))
    }

    override suspend fun readDataFrom(zipFile: ZipFile): DataResult {
        try {
            //
            // Reading the data
            //

            val dreams = zipFile.decode<ListBased.Dream>(DREAMS_FILE_NAME)
            val persons = zipFile.decode<ListBased.Person>(PERSONS_FILE_NAME)
            val tags = zipFile.decode<ListBased.Tag>(TAGS_FILE_NAME)
            val emotions =
                zipFile.decode<ListBased.Emotion>(EMOTIONS_FILE_NAME)
            val dreamsToPersons =
                zipFile.decode<ListBased.DreamToPersons>(DREAMS_TO_PERSONS_FILE_NAME)
            val dreamsToEmotions =
                zipFile.decode<ListBased.DreamToEmotion>(DREAMS_TO_EMOTIONS_FILE_NAME)
            val dreamsToTags =
                zipFile.decode<ListBased.DreamToTag>(DREAMS_TO_TAGS_FILE_NAME)
            val personsToTags =
                zipFile.decode<ListBased.PersonToTag>(PERSONS_TO_TAGS_FILE_NAME)

            //
            // Checking data integrity
            //
            val referencedDreams =
                (dreamsToPersons.map { it.dreamId } union dreamsToEmotions.map { it.dreamId } union dreamsToTags.map { it.dreamId }).distinct()
            val referencedEmotions = dreamsToEmotions.map { it.emotionId }.distinct()
            val referencedPersons = dreamsToPersons.map { it.personId }.distinct()
            val referencedTags =
                (dreamsToTags.map { it.tagId } union personsToTags.map { it.tagId }).distinct()
            val referencedConfids = dreams.map { it.confidentiality }.distinct()

            val dreamsValid = referencedDreams.all { dreamId -> dreams.any { it.id == dreamId } }
            val emotionsValid =
                referencedEmotions.all { emotionId -> emotions.any { it.id == emotionId } }
            val personsValid =
                referencedPersons.all { personId -> persons.any { it.id == personId } }
            val tagsValid = referencedTags.all { tagId -> tags.any { it.id == tagId } }
            val confidsValid =
                referencedConfids.all { confid -> Confidentiality.entries.any { it.name == confid } }

            val integrityViolated =
                !dreamsValid || !emotionsValid || !personsValid || !confidsValid || !tagsValid
            if (integrityViolated) return DataResult.DataIntegrity

            //
            // Saving the data
            //
            tags.forEach { tag ->
                val input = SaveTagUseCase.I(tag.toModel())
                saveTag(input)
            }
            persons.forEach { person ->
                val tagsForPerson = personsToTags
                    .filter { it.personId == person.id }
                    .map { it.tagId }
                    .map { tags.find { tag -> tag.id == it }!! }
                    .map { it.toModel() }

                val input = SavePersonUseCase.I(
                    person = person.toModel(),
                    tags = tagsForPerson
                )
                savePerson(input)
            }
            emotions.forEach { emotion ->
                saveEmotion(emotion.toModel())
            }

            dreams.forEach { dream ->
                val persons = dreamsToPersons
                    .filter { it.dreamId == dream.id }
                    .map { it.personId }
                    .mapNotNull { id -> persons.find { it.id == id } }
                    .map { it.toModel() }
                val emotions = dreamsToEmotions
                    .filter { it.dreamId == dream.id }
                    .map { it.emotionId }
                    .mapNotNull { id -> emotions.find { it.id == id } }
                    .map { it.toModel() }
                val tags = dreamsToTags
                    .filter { it.dreamId == dream.id }
                    .map { it.tagId }
                    .mapNotNull { id -> tags.find { it.id == id } }
                    .map { it.toModel() }

                val input = SaveDreamUseCase.I(dream.toModel(), persons, emotions, tags)
                saveDream(input)
            }

            return DataResult.Success(
                emotionCount = emotions.size,
                dreamCount = dreams.size,
                personCount = persons.size,
                firstDream = dreams.minOf { it.dreamt }.let(Instant::fromEpochMilliseconds),
                lastDream = dreams.maxOf { it.dreamt }.let(Instant::fromEpochMilliseconds)
            )
        } catch (e: SerializationException) {
            captureException(e)
            return DataResult.DataFormat
        } catch (e: IOException) {
            captureException(e)
            return DataResult.ReadError
        }
    }

    companion object {
        private const val DREAMS_FILE_NAME = "dreams"
        private const val PERSONS_FILE_NAME = "persons"
        private const val EMOTIONS_FILE_NAME = "emotions"
        private const val DREAMS_TO_EMOTIONS_FILE_NAME = "dreams_emotions"
        private const val DREAMS_TO_PERSONS_FILE_NAME = "dreams_persons"
        private const val DREAMS_TO_TAGS_FILE_NAME = "dreams_tags"
        private const val PERSONS_TO_TAGS_FILE_NAME = "persons_tags"
        private const val TAGS_FILE_NAME = "tags"
    }

    @OptIn(ExperimentalSerializationApi::class)
    private inline fun <reified T> ZipFile.decode(name: String): List<T> {
        val source = sourceForName(name, fileType)
            ?: throw IllegalArgumentException("No zip entry found for name $name")

        return when (fileType) {
            IOFileType.Json -> Json.decodeFromSource(source)
            IOFileType.Yaml -> Yaml.decodeFromString(source.readByteArray().decodeToString())
            IOFileType.Xml -> throw IllegalArgumentException("Invalid file type: $fileType")
        }
    }
}