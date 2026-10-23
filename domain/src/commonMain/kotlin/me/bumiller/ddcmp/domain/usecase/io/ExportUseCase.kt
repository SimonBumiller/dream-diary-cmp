package me.bumiller.ddcmp.domain.usecase.io

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.io.BackupExporterFactory
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.domain.usecase.io.ExportUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

class ExportUseCase(
    private val exporterFactory: BackupExporterFactory,
    private val exportRepo: ExportLogRepository
): UseCase<I, Long> {

    override suspend fun invoke(i: I): Long {
        val exporter = exporterFactory.createExporter(i.options)

        val result = exporter.export(i.destination)
        val id = exportRepo.save(result)

        return id
    }

    /**
     * Input for the usecase.
     */
    data class I(

        /**
         * Where the export should be saved to.
         */
        val destination: PlatformFile,

        /**
         * The options of the export.
         */
        val options: ExportOption

    )

}