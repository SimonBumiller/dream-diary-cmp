package me.bumiller.ddcmp.core.io.factory

import me.bumiller.ddcmp.core.io.importer.LegacyImporter
import me.bumiller.ddcmp.core.io.importer.ListBasedImporter
import me.bumiller.ddcmp.core.io.importer.XmlImporter
import me.bumiller.ddcmp.core.io.importer.backgroundProxy
import me.bumiller.ddcmp.domain.io.BackupImporter
import me.bumiller.ddcmp.domain.io.BackupImporterFactory
import me.bumiller.ddcmp.domain.model.IOFileType
import me.bumiller.ddcmp.domain.model.ImportOption
import me.bumiller.ddcmp.domain.model.Settings
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import kotlin.time.ExperimentalTime

internal class ImporterFactoryImpl(
    private val saveDream: SaveDreamUseCase,
    private val savePerson: SavePersonUseCase,
    private val saveEmotion: SaveEmotionUseCase
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

            is ImportOption.Normal -> when (options.contentFileType) {
                IOFileType.Json, IOFileType.Yaml -> ListBasedImporter(
                    saveDream,
                    savePerson,
                    saveEmotion,
                    options.contentFileType
                )

                IOFileType.Xml -> XmlImporter(saveDream, savePerson, saveEmotion)
            }
        }.backgroundProxy()
    }
}