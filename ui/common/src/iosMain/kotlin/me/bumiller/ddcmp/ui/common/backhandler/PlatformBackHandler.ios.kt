package me.bumiller.ddcmp.ui.common.backhandler

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(onBack: () -> Unit) {
    // Not handling back clicks on ios
}