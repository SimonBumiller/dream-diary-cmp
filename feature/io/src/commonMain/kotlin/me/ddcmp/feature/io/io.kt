package me.ddcmp.feature.io

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import me.ddcmp.feature.io.entries.export_configuration.ExportConfigEntry
import me.ddcmp.feature.io.entries.export_result.ExportResultEntry
import me.ddcmp.feature.io.entries.import_configuration.ImportConfigEntry
import me.ddcmp.feature.io.entries.import_result.ImportResultEntry
import me.ddcmp.feature.io.navigation.IORoutes

/**
 * Navigates to the export screen.
 */
public fun NavController.navigateToExport(): Unit =
    navigate(IORoutes.ExportConfig)

/**
 * Navigates to the import screen.
 */
public fun NavController.navigateToImport(): Unit =
    navigate(IORoutes.ImportConfig)

/**
 * Adds the io routes to the navigation graph.
 */
public fun NavGraphBuilder.io(
    navController: NavController
) {
    dialog<IORoutes.ExportResult> {
        ExportResultEntry(
            onContinue = {
                when (it) {
                    true -> {
                        navController.popBackStack<IORoutes.ExportConfig>(inclusive = true)
                    }
                    false -> navController.popBackStack()
                }
            }
        )
    }

    composable<IORoutes.ExportConfig> {
        ExportConfigEntry(
            onExport = { navController.navigate(IORoutes.ExportResult(it)) },
            onReturn = navController::popBackStack
        )
    }

    dialog<IORoutes.ImportResult> {
        ImportResultEntry(
            onContinue = {
                navController.popBackStack<IORoutes.ImportConfig>(inclusive = true)
            }
        )
    }

    composable<IORoutes.ImportConfig> {
        ImportConfigEntry(
            onImport = { resultId ->
                navController.navigate(IORoutes.ImportResult(resultId))
            },
            onReturn = navController::popBackStack
        )
    }
}