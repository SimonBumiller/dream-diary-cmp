package me.bumiller.ddcmp.ui.common.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.biometric_description
import dream_diary_cmp.ui.common.generated.resources.biometric_title
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun BiometricContainer(state: BiometricAuthState, onResult: (BiometricResult) -> Unit) {
    val manager = rememberBiometricPromptManager()

    val title = stringResource(Res.string.biometric_title)
    val description = stringResource(Res.string.biometric_description)

    LaunchedEffect(Unit) {
        manager.promptResults.collect(onResult)
    }
    LaunchedEffect(Unit) {
        state.authRequests.collect {
            manager.showBiometricPrompt(title, description)
        }
    }
}