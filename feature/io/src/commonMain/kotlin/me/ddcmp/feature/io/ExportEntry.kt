package me.ddcmp.feature.io

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import me.ddcmp.feature.io.entries.export_configuration.ExportConfigEntry
import me.ddcmp.feature.io.entries.export_result.ExportResultEntry
import me.ddcmp.feature.io.navigation.IORoutes

@Composable
fun ExportEntry(
    onBack: () -> Unit,
    onFinished: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = IORoutes.ExportConfig
    ) {
        dialog<IORoutes.ExportResult> {
            ExportResultEntry(
                onContinue = {
                    when(it) {
                        true -> onFinished()
                        false -> onBack()
                    }
                }
            )
        }

        composable<IORoutes.ExportConfig> {
            ExportConfigEntry(
                onExport = { navController.navigate(IORoutes.ExportResult(it)) },
                onReturn = onBack
            )
        }
    }
}