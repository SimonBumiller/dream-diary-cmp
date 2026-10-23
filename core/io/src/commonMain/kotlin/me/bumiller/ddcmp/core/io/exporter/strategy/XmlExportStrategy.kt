package me.bumiller.ddcmp.core.io.exporter.strategy

import kotlinx.coroutines.flow.first
import kotlinx.io.IOException
import me.bumiller.ddcmp.common.captureException
import me.bumiller.ddcmp.core.io.common.configureEntry
import me.bumiller.ddcmp.core.io.exporter.ZipFileExporter.DataExportStrategy
import me.bumiller.ddcmp.core.io.model.Xml
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import nl.adaptivity.xmlutil.serialization.XML
import no.synth.kmpzip.zip.ZipOutputStream

internal class XmlExportStrategy(
    private val allDreams: AllDreamUseCase,
    private val allPersons: AllPersonUseCase,
    private val allEmotions: AllEmotionsUseCase,
    private val allTags: AllTagUseCase
) : DataExportStrategy {

    override suspend fun writeDataInto(
        zipStream: ZipOutputStream
    ): DataExportStrategy.DataResult {
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

        try {
            zipStream.apply {
                writeXml(Xml.DreamsRoot(dreams), DREAMS_FILE_NAME)
                writeXml(Xml.PersonsRoot(persons), PERSONS_FILE_NAME)
                writeXml(Xml.EmotionsRoot(emotions), EMOTIONS_FILE_NAME)
                writeXml(Xml.TagsRoot(tags), TAGS_FILE_NAME)
            }
        } catch(e: IOException) {
            captureException(e)
            return DataExportStrategy.DataResult.WriteError(e)
        }

        return DataExportStrategy.DataResult.Success
    }

    companion object {
        private const val DREAMS_FILE_NAME = "dreams"
        private const val PERSONS_FILE_NAME = "persons"
        private const val EMOTIONS_FILE_NAME = "emotions"
        private const val TAGS_FILE_NAME = "tags"
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