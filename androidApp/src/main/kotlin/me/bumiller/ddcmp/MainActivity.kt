package me.bumiller.ddcmp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        var didAppLoad = false

        splash.setKeepOnScreenCondition {
            !didAppLoad
        }

        setContent {
            AppRoot(
                sizeClass = calculateWindowSizeClass(this),
                onScreenReady = { didAppLoad = true }
            )
        }
    }
}