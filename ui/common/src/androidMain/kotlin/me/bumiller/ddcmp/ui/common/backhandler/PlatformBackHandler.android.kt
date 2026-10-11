package me.bumiller.ddcmp.ui.common.backhandler

import androidx.compose.runtime.Composable
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.div

@Composable
actual fun PlatformBackHandler(onBack: () -> Unit) {
    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        onBackCompleted = onBack
    )
}