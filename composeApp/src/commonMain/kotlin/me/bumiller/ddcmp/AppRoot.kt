package me.bumiller.ddcmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.flow.collectLatest
import me.bumiller.ddcmp.navigation.MainRoutes
import me.bumiller.ddcmp.theme.AppTheme
import me.bumiller.ddcmp.theme.systemTheme
import me.ddcmp.feature.home.HomeEntry
import me.ddcmp.feature.home.OnboardingEntry
import me.ddcmp.feature.home.SettingsEntry
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

/**
 * The root composable for the app.
 *
 * From here on, everything is shared on all platforms.
 *
 * @param onScreenReady The callback that is called when the screen is ready to display the app, i.e. remove a loading screen.
 */
@Composable
fun AppRoot(
    onScreenReady: () -> Unit = {}
) = KoinContext {
    val viewmodel = koinViewModel<AppViewmodel>()

    // Wait until the state is available
    LaunchedEffect(Unit) {
        viewmodel.state.collectLatest { state ->
            if (state != null) {
                onScreenReady()
            }
        }
    }

    val _state by viewmodel.state.collectAsState()
    val uiState by viewmodel.uiState.collectAsState()

    // If the state is not yet present, or the backstack is still empty, we simply show nothing.
    // Works because splash screen should be visible atm.
    val state = _state ?: return@KoinContext
    if (uiState.navBackStack.isEmpty()) return@KoinContext

    AppTheme(
        mode = state.settings.colorMode,
        theme = state.settings.colorTheme,
        systemTheme = { systemTheme(it) }
    ) {
        NavDisplay(
            modifier = Modifier
                .fillMaxSize(),
            backStack = uiState.navBackStack,
            onBack = { viewmodel.navigateBack() },
            entryProvider = entryProvider {
                entry<MainRoutes.Home> {
                    HomeEntry()
                }

                entry<MainRoutes.Onboarding> {
                    OnboardingEntry()
                }

                entry<MainRoutes.Settings> {
                    SettingsEntry()
                }
            }
        )
    }
}