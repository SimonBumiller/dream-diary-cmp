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

/**
 * Declaration of what features a platform supports.
 */
expect object PlatformFeatures {

    /**
     * Whether the platform supports biometric authentication.
     */
    val supportsBiometricAuth: Boolean

    /**
     * Whether the platform supports periodic backups.
     */
    val supportsPeriodicBackups: Boolean

    /**
     * Whether the platform supports dynamic theming.
     */
    val supportsDynamicTheming: Boolean

}