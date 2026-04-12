package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.model.Settings

/**
 * Data store interface for the settings.
 */
interface SettingsStore {

    /**
     * The current settings.
     */
    val settings: Flow<Settings>

    /**
     * Updates the settibngs.
     */
    suspend fun save(settings: Settings)

}