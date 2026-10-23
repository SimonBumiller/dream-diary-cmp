package me.bumiller.ddcmp.core.io.exporter

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.core.io.common.configureEntry
import me.bumiller.ddcmp.core.io.model.Xml
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import nl.adaptivity.xmlutil.serialization.XML
import no.synth.kmpzip.zip.ZipOutputStream

private const val DREAMS_FILE_NAME = "dreams"
private const val PERSONS_FILE_NAME = "persons"
private const val EMOTIONS_FILE_NAME = "emotions"
private const val TAGS_FILE_NAME = "tags"

internal class XmlExporter(
    private val allDreams: AllDreamUseCase,
    private val allPersons: AllPersonUseCase,
    private val allEmotions: AllEmotionsUseCase,
    private val allTags: AllTagUseCase
) : ZipFileExporter() {

    override suspend fun aggregateZipFile(
        zipStream: ZipOutputStream
    ): ExportResult? {
        val dreams = allDreams(AllDreamUseCase.I())
            .first()
            .map(Xml.Dream::fromModel)

        val emotions = allEmotions(AllEmotionsUseCase.I())
            .first()
            .map(Xml.Emotion::fromModel)

        val persons = allPersons(AllPersonUseCase.I())
            .first()
            .map(Xml.Person::fromModel)

        val tags = allTags(AllTagUseCase.I())
            .first()
            .map(Xml.Tag::fromModel)

        zipStream.apply {
            writeXml(Xml.DreamsRoot(dreams), DREAMS_FILE_NAME)
            writeXml(Xml.PersonsRoot(persons), PERSONS_FILE_NAME)
            writeXml(Xml.EmotionsRoot(emotions), EMOTIONS_FILE_NAME)
            writeXml(Xml.TagsRoot(tags), TAGS_FILE_NAME)
        }

        return null
    }

    private inline fun <reified T : Any> ZipOutputStream.writeXml(
        root: T,
        fileName: String
    ) {
        configureEntry(fileName, IOFileType.Xml.exportFileEx) {
            XML.v1.encodeToString(root)
                .encodeToByteArray()
                .let(::write)
        }
    }
}