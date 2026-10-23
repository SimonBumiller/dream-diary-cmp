package me.bumiller.ddcmp.core.io.factory

import me.bumiller.ddcmp.core.io.importer.LegacyImporter
import me.bumiller.ddcmp.core.io.importer.ZipFileImporter
import me.bumiller.ddcmp.core.io.importer.strategy.ListBasedImportStrategy
import me.bumiller.ddcmp.core.io.importer.strategy.XmlImportStrategy
import me.bumiller.ddcmp.domain.io.BackupImporter
import me.bumiller.ddcmp.domain.io.BackupImporterFactory
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.model.io.ImportOption
import me.bumiller.ddcmp.domain.model.settings.Settings
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.SaveTagUseCase

internal class ImporterFactoryImpl(
    private val saveDream: SaveDreamUseCase,
    private val savePerson: SavePersonUseCase,
    private val saveEmotion: SaveEmotionUseCase,
    private val saveTag: SaveTagUseCase
) : BackupImporterFactory {

    override fun createImporter(options: ImportOption, settings: Settings): BackupImporter =
        when (options) {
            is ImportOption.Legacy -> LegacyImporter(
                timeZone = options.timeZone,
                transferFavorites = options.transferFavorites,
                transferRelations = options.transferRelations,
                defaultConfidentiality = settings.defaultConfidentiality,
                createDream = saveDream,
                createPerson = savePerson,
                createTag = saveTag
            )

            is ImportOption.Normal -> when (options.contentFileType) {
                IOFileType.Json, IOFileType.Yaml -> ListBasedImportStrategy(
                    saveDream,
                    savePerson,
                    saveEmotion,
                    saveTag,
                    options.contentFileType
                )

                IOFileType.Xml -> XmlImportStrategy(saveDream, savePerson, saveEmotion, saveTag)
            }.let(::ZipFileImporter)
        }
}