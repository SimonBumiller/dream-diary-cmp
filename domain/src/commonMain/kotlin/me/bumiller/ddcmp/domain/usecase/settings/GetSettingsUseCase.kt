package me.bumiller.ddcmp.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.SettingsStore
import me.bumiller.ddcmp.domain.model.settings.Settings
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase

public class GetSettingsUseCase(
    private val store: SettingsStore
) : FlowUseCase<Unit, Settings> {
    override fun invoke(i: Unit): Flow<Settings> = store.settings
}