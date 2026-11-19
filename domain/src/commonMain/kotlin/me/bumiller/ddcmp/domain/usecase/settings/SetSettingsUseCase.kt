package me.bumiller.ddcmp.domain.usecase.settings

import me.bumiller.ddcmp.domain.infrastructure.SettingsStore
import me.bumiller.ddcmp.domain.model.settings.Settings
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

public class SetSettingsUseCase(
    private val store: SettingsStore
) : UseCase<Settings, Unit> {
    override suspend fun invoke(i: Settings): Unit = store.save(i)
}