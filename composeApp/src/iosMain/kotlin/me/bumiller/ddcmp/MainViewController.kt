package me.bumiller.ddcmp

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()

    return ComposeUIViewController { App() }
}