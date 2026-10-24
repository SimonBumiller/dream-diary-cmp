package me.bumiller.ddcmp.core.io.factory

import me.bumiller.ddcmp.core.io.exporter.ZipFileExporter
import me.bumiller.ddcmp.core.io.exporter.strategy.LegacyExportStrategy
import me.bumiller.ddcmp.core.io.exporter.strategy.ListBasedExportStrategy
import me.bumiller.ddcmp.core.io.exporter.strategy.XmlExportStrategy
import me.bumiller.ddcmp.domain.io.AttachmentStorage
import me.bumiller.ddcmp.domain.io.BackupExporterFactory
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.attachment.GetAttachmentsUseCase
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase

internal class ExporterFactoryImpl(
    private val allDreams: AllDreamUseCase,
    private val allPersons: AllPersonUseCase,
    private val allEmotions: AllEmotionsUseCase,
    private val allTags: AllTagUseCase,
    private val allAttachmentMetas: GetAttachmentsUseCase
) : BackupExporterFactory {

    // Exists for further expansion of ExportOption.
    @Suppress("UnusedReceiverParameter")
    private fun ExportOption.isZipBased() = true

    override fun createExporter(options: ExportOption) =
        when (options.isZipBased()) {
            false -> throw IllegalArgumentException("Non-zip based exports are not supported.")
            true -> when (options) {
                ExportOption.Legacy -> LegacyExportStrategy(allDreams, allPersons)
                is ExportOption.Normal -> when (options.fileType) {
                    IOFileType.Json, IOFileType.Yaml -> ListBasedExportStrategy(
                        allDreams,
                        allPersons,
                        allEmotions,
                        allTags,
                        allAttachmentMetas,
                        options.fileType
                    )

                    IOFileType.Xml -> XmlExportStrategy(allDreams, allPersons, allEmotions, allTags, allAttachmentMetas)
                }
            }.let { ZipFileExporter(it, allAttachmentMetas) }
        }
}