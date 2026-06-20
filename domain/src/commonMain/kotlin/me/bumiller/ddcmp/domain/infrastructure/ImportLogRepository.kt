package me.bumiller.ddcmp.domain.infrastructure

import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.ImportResult

/**
 * Interface for import results.
 */
interface ImportLogRepository: Repository<ImportResult>