package me.bumiller.ddcmp.core.io.exporter

import kotlinx.coroutines.flow.first
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.bumiller.ddcmp.core.io.common.configureEntry
import me.bumiller.ddcmp.core.io.model.ListBased
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import net.mamoe.yamlkt.Yaml
import no.synth.kmpzip.zip.ZipOutputStream
import kotlin.time.ExperimentalTime

private const val DREAMS_FILE_NAME = "dreams"
private const val PERSONS_FILE_NAME = "persons"
private const val EMOTIONS_FILE_NAME = "emotions"
private const val TAGS_FILE_NAME = "tags"
private const val DREAMS_TO_EMOTIONS_FILE_NAME = "dreams_emotions"
private const val DREAMS_TO_PERSONS_FILE_NAME = "dreams_persons"
private const val DREAMS_TO_TAGS_FILE_NAME = "dreams_tags"
private const val PERSONS_TO_TAGS_FILE_NAME = "persons_tags"

@OptIn(ExperimentalTime::class)
internal class ListBasedExporter(
    private val allDreams: AllDreamUseCase,
    private val allPersons: AllPersonUseCase,
    private val allEmotions: AllEmotionsUseCase,
    private val allTags: AllTagUseCase,
    private val fileType: IOFileType
) : ZipFileExporter() {

    init {
        require(fileType in setOf(IOFileType.Json, IOFileType.Yaml))
    }

    override suspend fun aggregateZipFile(
        zipStream: ZipOutputStream
    ): ExportResult? {
        val dreams = mutableListOf<ListBased.Dream>()

        val dreamsToPersons = mutableListOf<ListBased.DreamToPersons>()
        val dreamsToEmotions = mutableListOf<ListBased.DreamToEmotion>()
        val dreamsToTags = mutableListOf<ListBased.DreamToTag>()

        allDreams(AllDreamUseCase.I())
            .first()
            .forEach { model ->
                dreams.add(ListBased.Dream.fromModel(model.dream))
                dreamsToPersons.addAll(
                    ListBased.DreamToPersons.fromModel(model)
                )
                dreamsToEmotions.addAll(
                    ListBased.DreamToEmotion.fromModel(model)
                )
                dreamsToTags.addAll(
                    ListBased.DreamToTag.fromModel(model)
                )
            }

        val persons = mutableListOf<ListBased.Person>()
        val personsToTags = mutableListOf<ListBased.PersonToTag>()

        allPersons(AllPersonUseCase.I())
            .first()
            .forEach { model ->
                persons.add(ListBased.Person.fromModel(model))
                personsToTags.addAll(
                    ListBased.PersonToTag.fromModel(model)
                )
            }

        val emotions = allEmotions(AllEmotionsUseCase.I())
            .first()
            .map(ListBased.Emotion::fromModel)

        val tags = allTags(AllTagUseCase.I())
            .first()
            .map(ListBased.Tag::fromModel)

        zipStream.apply {
            writeListBased(dreams, DREAMS_FILE_NAME)
            writeListBased(emotions, EMOTIONS_FILE_NAME)
            writeListBased(persons, PERSONS_FILE_NAME)
            writeListBased(tags, TAGS_FILE_NAME)
            writeListBased(dreamsToPersons, DREAMS_TO_PERSONS_FILE_NAME)
            writeListBased(dreamsToEmotions, DREAMS_TO_EMOTIONS_FILE_NAME)
            writeListBased(dreamsToTags, DREAMS_TO_TAGS_FILE_NAME)
            writeListBased(personsToTags, PERSONS_TO_TAGS_FILE_NAME)
        }

        return null
    }

    private inline fun <reified T> ZipOutputStream.writeListBased(
        list: List<T>,
        fileName: String
    ) {
        configureEntry(fileName, fileType.exportFileEx) {
            encodeWrite(list)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private inline fun <reified T> ZipOutputStream.encodeWrite(
        items: T
    ) {
        when (fileType) {
            IOFileType.Json ->
                Json.encodeToString(items)
                    .encodeToByteArray()
                    .let(::write)

            IOFileType.Yaml ->
                Yaml.encodeToString(items)
                    .encodeToByteArray()
                    .let(::write)

            else -> error("Unsupported file type: $fileType")
        }
    }
}