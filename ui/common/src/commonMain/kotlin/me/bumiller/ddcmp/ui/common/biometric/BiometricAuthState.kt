package me.bumiller.ddcmp.ui.common.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class BiometricAuthState {

    private val _authRequests = MutableSharedFlow<Unit>(1)
    val authRequests: Flow<Unit> = _authRequests

    fun requestAuth() {
        _authRequests.tryEmit(Unit)
    }

}

@Composable
fun rememberBiometricState() = remember { BiometricAuthState() }