package me.ddcmp.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.ddcmp.feature.settings.entries.order_config.OrderConfigEntry
import me.ddcmp.feature.settings.entries.order_detail.OrderDetailEntry
import me.ddcmp.feature.settings.entries.root.SettingsRootEntry
import me.ddcmp.feature.settings.navigation.SettingsRoutes

/**
 * Entry for the settings section of the app.
 */
public fun NavGraphBuilder.settings(
    onGoToExport: () -> Unit,
    onGoToImport: () -> Unit,
    navController: NavController
) {
    composable<SettingsRoutes.Root> {
        SettingsRootEntry(
            onBack = { navController.popBackStack() },
            onGoToExport = onGoToExport,
            onGoToImport = onGoToImport,
            onGoToOrderConfig = { navController.navigate(SettingsRoutes.OrderConfig) },
            onGoToOrder = { navController.navigate(SettingsRoutes.OrderDetail(it)) }
        )
    }

    composable<SettingsRoutes.OrderConfig> {
        OrderConfigEntry(
            onCancel = { navController.popBackStack() },
            onConfigured = {
                navController.navigate(SettingsRoutes.OrderDetail(it)) {
                    popUpTo<SettingsRoutes.OrderConfig> {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<SettingsRoutes.OrderDetail> {
        OrderDetailEntry(
            onGoBack = { navController.popBackStack() },
            onFreshSetup = {
                navController.navigate(SettingsRoutes.OrderConfig) {
                    popUpTo<SettingsRoutes.OrderDetail> {
                        inclusive = true
                    }
                }
            }
        )
    }
}