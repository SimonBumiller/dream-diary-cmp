package me.bumiller.ddcmp.ui.common.biometric

public sealed interface BiometricResult {
    public data object HardwareUnavailable : BiometricResult
    public data object FeatureUnavailable : BiometricResult
    public data class AuthenticationError(val error: String) : BiometricResult
    public data object AuthenticationFailed : BiometricResult
    public data object AuthenticationSuccess : BiometricResult
    public data object AuthenticationNotSet : BiometricResult
}