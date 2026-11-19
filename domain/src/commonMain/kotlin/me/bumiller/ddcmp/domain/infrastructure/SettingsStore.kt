package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.model.settings.Settings

/**
 * Data store interface for the settings.
 */
public interface SettingsStore {

    /**
     * The current settings.
     */
    public val settings: Flow<Settings>

    /**
     * Updates the settibngs.
     */
    public suspend fun save(settings: Settings)

}