package me.bumiller.ddcmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import me.bumiller.ddcmp.domain.infrastructure.SettingsStore
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.settings.ColorMode
import me.bumiller.ddcmp.domain.model.settings.ColorTheme
import me.bumiller.ddcmp.domain.model.settings.Settings

class DataStoreSettingsStore(private val dataStore: DataStore<Preferences>) :
    SettingsStore {

    // 2. Define preference keys
    private object Keys {
        val UNLOCK = booleanPreferencesKey("unlock")
        val COLOR_MODE = stringPreferencesKey("color_mode")
        val COLOR_THEME = stringPreferencesKey("color_theme")
        val DEFAULT_CONFIDENTIALITY = stringPreferencesKey("default_confidentiality")
        val ONBOARDING_FINISHED = booleanPreferencesKey("onboarding_finished")
        val STATISTICS_DATE_RANGE = stringPreferencesKey("statistics_date_range")
    }

    // 3. Expose settings as a Flow
    override val settings: Flow<Settings> = dataStore.data
        .map { prefs ->
            Settings(
                unlock = prefs[Keys.UNLOCK] ?: Settings.Default.unlock,
                colorMode = prefs[Keys.COLOR_MODE]?.let { safeEnum<ColorMode>(it) }
                    ?: Settings.Default.colorMode,
                colorTheme = prefs[Keys.COLOR_THEME]?.let { safeEnum<ColorTheme>(it) }
                    ?: Settings.Default.colorTheme,
                defaultConfidentiality = prefs[Keys.DEFAULT_CONFIDENTIALITY]?.let {
                    safeEnum<Confidentiality>(
                        it
                    )
                }
                    ?: Settings.Default.defaultConfidentiality,
                onboardingFinished = prefs[Keys.ONBOARDING_FINISHED]
                    ?: Settings.Default.onboardingFinished,
                statisticsDateRange = parseDatePeriodSafe(prefs[Keys.STATISTICS_DATE_RANGE])
            )
        }

    private fun parseDatePeriodSafe(string: String?): DatePeriod? =
        if (string == null) null
        else try {
            DatePeriod.parse(string)
        } catch(_: IllegalArgumentException) { null }

    // 4. Save settings
    override suspend fun save(settings: Settings) {
        dataStore.edit { prefs ->
            prefs[Keys.UNLOCK] = settings.unlock
            prefs[Keys.COLOR_MODE] = settings.colorMode.name
            prefs[Keys.COLOR_THEME] = settings.colorTheme.name
            prefs[Keys.DEFAULT_CONFIDENTIALITY] = settings.defaultConfidentiality.name
            prefs[Keys.ONBOARDING_FINISHED] = settings.onboardingFinished
            prefs[Keys.STATISTICS_DATE_RANGE] = settings.statisticsDateRange.toString()
        }
    }

    // Helper: safely convert string to enum
    private inline fun <reified T : Enum<T>> safeEnum(value: String): T? {
        return try {
            enumValueOf<T>(value)
        } catch (_: Exception) {
            null
        }
    }
}