package me.ddcmp.feature.settings

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.vinceglb.filekit.PlatformFile
import me.ddcmp.feature.settings.entries.order_config.OrderConfigEntry
import me.ddcmp.feature.settings.entries.order_detail.OrderDetailEntry
import me.ddcmp.feature.settings.entries.root.SettingsRootEntry
import me.ddcmp.feature.settings.navigation.SettingsRoutes

/**
 * Entry for the settings section of the app.
 */
@Composable
fun SettingsEntry(
    onBack: () -> Unit,
    onGoToExport: () -> Unit,
    onGoToImport: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SettingsRoutes.Root
    ) {
        composable<SettingsRoutes.Root> {
            SettingsRootEntry(
                onBack = onBack,
                onGoToExport = onGoToExport,
                onGoToImport = onGoToImport,
                onGoToBackup = { navController.navigate(SettingsRoutes.OrderConfig) }
            )
        }

        composable<SettingsRoutes.OrderConfig> {
            OrderConfigEntry(
                onCancel = { navController.popBackStack() },
                onConfigured = {
                    // TODO: Actually make replace
                    navController.popBackStack()
                    navController.navigate(SettingsRoutes.OrderDetail(it))
                }
            )
        }

        composable<SettingsRoutes.OrderDetail> {
            OrderDetailEntry(
                onGoBack = { navController.popBackStack() },
                onFreshSetup = {
                    // TODO: Actually make replace
                    navController.popBackStack()
                    navController.navigate(SettingsRoutes.OrderConfig)
                }
            )
        }
    }
}