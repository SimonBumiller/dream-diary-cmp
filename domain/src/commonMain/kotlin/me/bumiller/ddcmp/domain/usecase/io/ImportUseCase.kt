package me.bumiller.ddcmp.domain.usecase.io

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.infrastructure.ImportLogRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.infrastructure.SettingsStore
import me.bumiller.ddcmp.domain.io.BackupImporterFactory
import me.bumiller.ddcmp.domain.model.ImportOption
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

/**
 * Usecase for importing data from a file to the local data.
 */
class ImportUseCase(
    private val personRepository: PersonRepository,
    private val dreamRepository: DreamRepository,
    private val emotionRepository: EmotionRepository,
    private val importerFactory: BackupImporterFactory,
    private val settingsStore: SettingsStore,
    private val importRepository: ImportLogRepository
): UseCase<ImportUseCase.I, Long> {

    override suspend fun invoke(i: I): Long {
        personRepository.deleteAll()
        dreamRepository.deleteAll()
        emotionRepository.deleteAll()

        val settings = settingsStore.settings.first()

        val importer = importerFactory.createImporter(i.options, settings)

        val result = importer.importBackup(i.file)
        val id = importRepository.save(result)

        return id
    }

    /**
     * The usecase input.
     */
    data class I(

        /**
         * The file to import from.
         */
        val file: PlatformFile,

        /**
         * The import options.
         */
        val options: ImportOption
    )

}