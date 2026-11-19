package me.bumiller.ddcmp.domain.model

public actual object PlatformFeatures {
    public actual val supportsBiometricAuth: Boolean = false
    public actual val supportsPeriodicBackups: Boolean = true
    public actual val supportsDynamicTheming: Boolean = false
}