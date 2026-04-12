package me.bumiller.ddcmp.domain.usecase.settings

import me.bumiller.ddcmp.domain.infrastructure.SettingsStore
import me.bumiller.ddcmp.domain.model.Settings
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase

class GetSettingsUseCase(
    val store: SettingsStore
) : FlowUseCase<Unit, Settings> {
    override fun invoke(i: Unit) = store.settings
}