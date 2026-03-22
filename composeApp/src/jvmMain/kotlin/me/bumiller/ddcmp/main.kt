package me.bumiller.ddcmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "dream-diary-cmp",
    ) {
        App()
    }
}