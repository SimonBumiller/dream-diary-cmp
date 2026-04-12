package me.bumiller.ddcmp.domain.model

/**
 * Information about the current platform.
 */
data class PlatformInfo (

    /**
     * Whether the platform allows locking the app.
     */
    val lockable: Boolean,

    /**
     * Whether the platform supports dynamic themes.
     */
    val dynamicTheming: Boolean

)