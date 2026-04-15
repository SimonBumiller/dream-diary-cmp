package me.bumiller.ddcmp.ui.common.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun BiometricContainer(state: BiometricAuthState, onResult: (BiometricResult) -> Unit) {
    val manager = rememberBiometricPromptManager()

    LaunchedEffect(Unit) {
        manager.promptResults.collect(onResult)
    }
    LaunchedEffect(Unit) {
        state.authRequests.collect {
            println("Received auth request inside BiometricContainer")
            manager.showBiometricPrompt("Authenticate", "Yo authenticate please")
        }
    }
}