package me.bumiller.ddcmp.domain.usecase.io

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.ImportLogRepository
import me.bumiller.ddcmp.domain.model.ImportResult
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

class GetImportLogUsecase(
    private val importLogRepository: ImportLogRepository
): UseCase<Long, ImportResult?> {
    override suspend fun invoke(i: Long): ImportResult?
        = importLogRepository.id(i).first()
}