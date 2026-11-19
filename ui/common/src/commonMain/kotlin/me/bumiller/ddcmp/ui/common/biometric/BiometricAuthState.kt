package me.bumiller.ddcmp.ui.common.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

public class BiometricAuthState {

    private val _authRequests = MutableSharedFlow<Unit>(1)
    public val authRequests: Flow<Unit> = _authRequests

    public fun requestAuth() {
        _authRequests.tryEmit(Unit)
    }

}

@Composable
public fun rememberBiometricState(): BiometricAuthState = remember { BiometricAuthState() }