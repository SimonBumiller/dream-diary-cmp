package me.bumiller.ddcmp.domain.infrastructure

import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.ExportResult
import me.bumiller.ddcmp.domain.model.ImportResult

/**
 * Interface for export results.
 */
interface ExportLogRepository: Repository<ExportResult>