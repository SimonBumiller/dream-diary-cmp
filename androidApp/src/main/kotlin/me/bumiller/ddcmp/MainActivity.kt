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
        val splash = installSplashScreen()
        enableEdgeToEdge()
        /*
         * An action bar is displayed by applying the 'Theme.App.Starting' theme.
         * This custom theme is needed because we want to display a splash screen.
         * I don't know why an action bar is displayed.
         * See https://stackoverflow.com/questions/71584663/splash-screen-api-is-showing-action-bar-for-android-12
         */
        actionBar?.hide()
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