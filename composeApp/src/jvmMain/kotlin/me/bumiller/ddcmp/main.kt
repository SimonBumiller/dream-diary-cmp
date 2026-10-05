package me.bumiller.ddcmp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dream_diary_cmp.composeapp.generated.resources.Res
import dream_diary_cmp.composeapp.generated.resources.jvm_icon
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "dream-diary-cmp",
        icon = painterResource(Res.drawable.jvm_icon)
    ) {
        AppRoot(
            sizeClass = calculateWindowSizeClass()
        )
    }
}