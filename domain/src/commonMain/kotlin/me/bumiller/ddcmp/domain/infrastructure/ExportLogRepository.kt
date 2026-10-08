package me.bumiller.ddcmp.domain.infrastructure

import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.io.ExportResult

/**
 * Interface for export results.
 */
interface ExportLogRepository: Repository<ExportResult>