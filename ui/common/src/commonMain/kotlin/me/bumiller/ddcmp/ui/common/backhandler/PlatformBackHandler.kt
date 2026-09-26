package me.bumiller.ddcmp.ui.common.backhandler

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformBackHandler(onBack: () -> Unit)