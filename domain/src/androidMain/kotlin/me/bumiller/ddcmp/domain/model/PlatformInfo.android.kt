package me.bumiller.ddcmp.domain.model

import android.os.Build

actual object PlatformFeatures {
    actual val supportsBiometricAuth = true
    actual val supportsPeriodicBackups = true
    actual val supportsDynamicTheming = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}