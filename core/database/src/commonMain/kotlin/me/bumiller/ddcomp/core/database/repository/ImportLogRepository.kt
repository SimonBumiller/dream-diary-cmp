package me.bumiller.ddcomp.core.database.repository

import me.bumiller.ddcmp.domain.infrastructure.ImportLogRepository
import me.bumiller.ddcmp.domain.model.ImportResult
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.ImportLogDao
import me.bumiller.ddcomp.core.database.entity.ImportLogEntity
import me.bumiller.ddcomp.core.database.repository.base.SimpleBaseRepository

class ImportLogRepository(
    importDao: ImportLogDao
) : SimpleBaseRepository<ImportResult, ImportLogEntity>(
    dao = importDao,
    map = Mapping::import,
    reverseMap = Mapping.Reverse::import
), ImportLogRepository