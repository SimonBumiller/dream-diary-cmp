package me.bumiller.ddcmp.ui.common.backhandler

import androidx.compose.runtime.Composable

@Composable
public expect fun PlatformBackHandler(onBack: () -> Unit)