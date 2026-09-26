package me.bumiller.ddcmp.ui.common.biometric

import androidx.compose.runtime.Composable

@Composable
actual fun BiometricContainer(
    state: BiometricAuthState,
    onResult: (BiometricResult) -> Unit
) {
}