package me.bumiller.ddcmp.core.io

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.core.io.legacy.LegacyImporter
import me.bumiller.ddcmp.domain.io.BackupImporter
import me.bumiller.ddcmp.domain.io.BackupImporterFactory
import me.bumiller.ddcmp.domain.model.ImportOption
import me.bumiller.ddcmp.domain.model.ImportResult
import me.bumiller.ddcmp.domain.model.Settings
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamPersonM2MDao
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class ImporterFactoryImpl(
    private val dreamDao: DreamDao,
    private val personDao: PersonDao,
    private val dreamPersonM2MDao: DreamPersonM2MDao
): BackupImporterFactory {

    @OptIn(ExperimentalTime::class)
    override fun createImporter(options: ImportOption, settings: Settings): BackupImporter {
        return when (options) {
            is ImportOption.Legacy -> LegacyImporter(
                timeZone = options.timeZone,
                transferFavorites = options.transferFavorites,
                defaultConfidentiality = settings.defaultConfidentiality,
                dreamDao = dreamDao,
                personDao = personDao,
                dreamPersonM2MDao = dreamPersonM2MDao
            )
            is ImportOption.Normal -> object : BackupImporter {
                override suspend fun importBackup(file: PlatformFile): ImportResult {
                    return ImportResult.Failure.DataConsistency(0, Clock.System.now())
                }
            }
        }
    }

}