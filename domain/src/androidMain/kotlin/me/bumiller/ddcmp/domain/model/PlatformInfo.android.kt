package me.bumiller.ddcmp.domain.model

import android.os.Build

public actual object PlatformFeatures {
    public actual val supportsBiometricAuth: Boolean = true
    public actual val supportsPeriodicBackups: Boolean = true
    public actual val supportsDynamicTheming: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}