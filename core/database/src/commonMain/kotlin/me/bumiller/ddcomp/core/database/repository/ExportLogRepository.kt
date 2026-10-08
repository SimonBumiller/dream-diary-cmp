package me.bumiller.ddcomp.core.database.repository

import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.ExportLogDao
import me.bumiller.ddcomp.core.database.entity.ExportLogEntity
import me.bumiller.ddcomp.core.database.repository.base.SimpleBaseRepository

class ExportLogRepository(
    exportDao: ExportLogDao
) : SimpleBaseRepository<ExportResult, ExportLogEntity>(
    dao = exportDao,
    map = Mapping::export,
    reverseMap = Mapping.Reverse::export
), ExportLogRepository