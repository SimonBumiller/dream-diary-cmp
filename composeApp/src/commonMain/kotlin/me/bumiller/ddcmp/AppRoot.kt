package me.bumiller.ddcmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.bumiller.ddcmp.theme.AppTheme
import me.bumiller.ddcmp.theme.systemTheme
import me.ddcmp.feature.home.home
import me.ddcmp.feature.home.navigateToOverview
import me.ddcmp.feature.io.io
import me.ddcmp.feature.io.navigateToExport
import me.ddcmp.feature.io.navigateToImport
import me.ddcmp.feature.onboarding.navigateToOnboarding
import me.ddcmp.feature.onboarding.onboarding
import me.ddcmp.feature.settings.navigateToSettings
import me.ddcmp.feature.settings.settings
import org.koin.compose.viewmodel.koinViewModel

/**
 * The root composable for the app.
 *
 * From here on, everything is shared on all platforms.
 *
 * @param sizeClass THe window size class determined by the environment.
 * @param onScreenReady The callback that is called when the screen is ready to display the app, i.e. remove a loading screen.
 */
@Composable
fun AppRoot(
    sizeClass: WindowSizeClass,
    onScreenReady: () -> Unit = {}
) {
    val viewmodel = koinViewModel<AppViewmodel>()
    val navController = rememberNavController()

    val _state by viewmodel.state.collectAsState()
    val doOnboarding by viewmodel.doOnboarding.collectAsState()

    // If the state is not yet present, or the backstack is still empty, we simply show nothing.
    // Works because splash screen should be visible atm.
    val state = _state ?: return

    if (doOnboarding == null) return
    else if (doOnboarding!!) onScreenReady()

    LaunchedEffect(doOnboarding) {
        if (doOnboarding == true) navController.navigateToOnboarding()
        else if (doOnboarding == false) navController.navigateToOverview()
    }

    AppTheme(
        mode = state.settings.colorMode,
        theme = state.settings.colorTheme,
        sizeClass = sizeClass,
        systemTheme = { systemTheme(it) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavHost(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navController,
                startDestination = Unit // Unit is a special destination that simply displays blank.
            ) {
                // Special destination to not display anything when the splash screen is shown.
                composable<Unit> { }

                home(
                    onGoToSettings = navController::navigateToSettings,
                    onLoaded = onScreenReady,
                    navController = navController
                )

                io(
                    navController = navController
                )

                onboarding(
                    onFinished = navController::navigateToOverview,
                    onImport = navController::navigateToImport,
                    navController = navController
                )

                settings(
                    onGoToExport = navController::navigateToExport,
                    onGoToImport = navController::navigateToImport,
                    navController = navController
                )
            }
        }
    }
}