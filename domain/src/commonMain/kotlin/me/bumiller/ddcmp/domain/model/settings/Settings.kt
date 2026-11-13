package me.bumiller.ddcmp.domain.model.settings

import kotlinx.datetime.DatePeriod
import me.bumiller.ddcmp.domain.model.dream.Confidentiality

/**
 * Models the settings of the app.
 */
data class Settings(

    /**
     * Whether the app should be unlocked.
     */
    var unlock: Boolean,

    /**
     * The color mode.
     */
    var colorMode: ColorMode,

    /**
     * The color theme.
     */
    var colorTheme: ColorTheme,

    /**
     * The default confidentiality for every newly created dream.
     */
    var defaultConfidentiality: Confidentiality,

    /**
     * Whether the user finished the onboarding.
     */
    var onboardingFinished: Boolean,

    /**
     * The date range selected in a statistics screen.
     * Null means all time.
     */
    var statisticsDateRange: DatePeriod?

) {

    companion object {

        /**
         * The default settings instance to be used when none is available.
         */
        val Default = Settings(false, ColorMode.System, ColorTheme.App, Confidentiality.None, false, null)

    }

}
