package me.bumiller.ddcmp.core.io.exporter

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.core.io.model.Xml
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import nl.adaptivity.xmlutil.serialization.XML
import no.synth.kmpzip.zip.ZipOutputStream

private const val DREAMS_FILE_NAME = "dreams"
private const val PERSONS_FILE_NAME = "persons"
private const val EMOTIONS_FILE_NAME = "emotions"

internal class XmlExporter(
    private val allDreams: AllDreamUseCase,
    private val allPersons: AllPersonUseCase,
    private val allEmotions: AllEmotionsUseCase
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

        zipStream.apply {
            writeXml(Xml.DreamsRoot(dreams), DREAMS_FILE_NAME)
            writeXml(Xml.PersonsRoot(persons), PERSONS_FILE_NAME)
            writeXml(Xml.EmotionsRoot(emotions), EMOTIONS_FILE_NAME)
        }

        return null
    }

    private inline fun <reified T : Any> ZipOutputStream.writeXml(
        root: T,
        fileName: String
    ) {
        entryForName(fileName, IOFileType.Xml.exportFileEx) {
            XML.v1.encodeToString(root)
                .encodeToByteArray()
                .let(::write)
        }
    }
}