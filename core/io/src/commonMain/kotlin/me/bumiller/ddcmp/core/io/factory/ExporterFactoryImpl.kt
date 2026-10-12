package me.bumiller.ddcmp.core.io.factory

import me.bumiller.ddcmp.core.io.exporter.LegacyExporter
import me.bumiller.ddcmp.core.io.exporter.ListBasedExporter
import me.bumiller.ddcmp.core.io.exporter.XmlExporter
import me.bumiller.ddcmp.core.io.exporter.backgroundProxy
import me.bumiller.ddcmp.domain.io.BackupExporterFactory
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase

internal class ExporterFactoryImpl(
    private val allDreams: AllDreamUseCase,
    private val allPersons: AllPersonUseCase,
    private val allEmotions: AllEmotionsUseCase,
    private val allTags: AllTagUseCase
) : BackupExporterFactory {

    override fun createImporter(options: ExportOption) = when (options) {
        ExportOption.Legacy -> LegacyExporter(allDreams, allPersons)
        is ExportOption.Normal -> when (options.fileType) {
            IOFileType.Json, IOFileType.Yaml -> ListBasedExporter(
                allDreams,
                allPersons,
                allEmotions,
                allTags,
                options.fileType
            )

            IOFileType.Xml -> XmlExporter(allDreams, allPersons, allEmotions, allTags)
        }
    }.backgroundProxy()

}