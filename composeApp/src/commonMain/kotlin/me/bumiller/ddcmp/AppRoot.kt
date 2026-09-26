package me.bumiller.ddcmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import me.bumiller.ddcmp.navigation.MainRoutes
import me.bumiller.ddcmp.theme.AppTheme
import me.bumiller.ddcmp.theme.systemTheme
import me.ddcmp.feature.home.HomeEntry
import me.ddcmp.feature.home.SettingsEntry
import me.ddcmp.feature.import.ImportEntry
import me.ddcmp.feature.onboarding.OnboardingEntry
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

    // Wait until the state is available
    LaunchedEffect(Unit) {
        viewmodel.state.collectLatest { state ->
            if (state != null) {
                onScreenReady()
            }
        }
    }

    val _state by viewmodel.state.collectAsState()
    val initialEntry by viewmodel.initialEntry.collectAsState()

    // If the state is not yet present, or the backstack is still empty, we simply show nothing.
    // Works because splash screen should be visible atm.
    val state = _state ?: return
    if (initialEntry == null) return

    AppTheme(
        mode = state.settings.colorMode,
        theme = state.settings.colorTheme,
        sizeClass = sizeClass,
        systemTheme = { systemTheme(it) }
    ) {
        NavHost(
            modifier = Modifier
                .fillMaxSize(),
            navController = navController,
            startDestination = initialEntry!!
        ) {
            composable<MainRoutes.Home> {
                HomeEntry()
            }

            composable<MainRoutes.Import> {
                ImportEntry(
                    onFinished = {
                        navController.popBackStack()
                        navController.navigate(MainRoutes.Home)
                    }
                )
            }

            composable<MainRoutes.Onboarding> {
                OnboardingEntry(
                    onFinished = {
                        navController.navigate(MainRoutes.Home)
                    },
                    onImport = { navController.navigate(MainRoutes.Import) }
                )
            }

            composable<MainRoutes.Settings> {
                SettingsEntry()
            }
        }
    }
}