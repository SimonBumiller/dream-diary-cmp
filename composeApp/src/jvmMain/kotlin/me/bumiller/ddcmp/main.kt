package me.bumiller.ddcmp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "dream-diary-cmp",
    ) {
        AppRoot(
            sizeClass = calculateWindowSizeClass()
        )
    }
}