package me.bumiller.ddcmp.domain.usecase.io

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

class GetExportLogUsecase(
    private val exportLogRepository: ExportLogRepository
): UseCase<Long, ExportResult?> {
    override suspend fun invoke(i: Long): ExportResult?
        = exportLogRepository.id(i).first()
}