package me.bumiller.ddcmp.core.io.factory

import me.bumiller.ddcmp.core.io.exporter.LegacyExporter
import me.bumiller.ddcmp.core.io.exporter.ListBasedExporter
import me.bumiller.ddcmp.core.io.exporter.XmlExporter
import me.bumiller.ddcmp.core.io.exporter.backgroundProxy
import me.bumiller.ddcmp.domain.io.BackupExporterFactory
import me.bumiller.ddcmp.domain.model.ExportOption
import me.bumiller.ddcmp.domain.model.IOFileType
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase

internal class ExporterFactoryImpl(
    private val allDreams: AllDreamUseCase,
    private val allPersons: AllPersonUseCase,
    private val allEmotions: AllEmotionsUseCase
) : BackupExporterFactory {

    override fun createImporter(options: ExportOption) = when (options) {
        ExportOption.Legacy -> LegacyExporter(allDreams, allPersons)
        is ExportOption.Normal -> when (options.fileType) {
            IOFileType.Json, IOFileType.Yaml -> ListBasedExporter(
                allDreams,
                allPersons,
                allEmotions,
                options.fileType
            )

            IOFileType.Xml -> XmlExporter(allDreams, allPersons, allEmotions)
        }
    }.backgroundProxy()

}