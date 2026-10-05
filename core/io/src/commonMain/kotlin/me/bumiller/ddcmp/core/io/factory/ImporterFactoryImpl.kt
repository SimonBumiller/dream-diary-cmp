package me.bumiller.ddcmp.core.io.factory

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.core.io.importer.LegacyImporter
import me.bumiller.ddcmp.domain.io.BackupImporter
import me.bumiller.ddcmp.domain.io.BackupImporterFactory
import me.bumiller.ddcmp.domain.model.ImportOption
import me.bumiller.ddcmp.domain.model.ImportResult
import me.bumiller.ddcmp.domain.model.Settings
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class ImporterFactoryImpl(
    private val saveDream: SaveDreamUseCase,
    private val savePerson: SavePersonUseCase
) : BackupImporterFactory {

    @OptIn(ExperimentalTime::class)
    override fun createImporter(options: ImportOption, settings: Settings): BackupImporter {
        return when (options) {
            is ImportOption.Legacy -> LegacyImporter(
                timeZone = options.timeZone,
                transferFavorites = options.transferFavorites,
                defaultConfidentiality = settings.defaultConfidentiality,
                createDream = saveDream,
                createPerson = savePerson
            )

            is ImportOption.Normal -> object : BackupImporter {
                override suspend fun importBackup(file: PlatformFile): ImportResult {
                    return ImportResult.Failure.DataConsistency(0, Clock.System.now())
                }
            }
        }
    }

}