package me.bumiller.ddcmp.ui.common.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class BiometricAuthState {

    private val _authRequests = Channel<Unit>()
    val authRequests: Flow<Unit> = _authRequests.receiveAsFlow()

    fun requestAuth() {
        _authRequests.trySend(Unit)
    }

}

@Composable
fun rememberBiometricState() = remember { BiometricAuthState() }