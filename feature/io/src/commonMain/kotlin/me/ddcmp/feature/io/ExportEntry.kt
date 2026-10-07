package me.ddcmp.feature.io

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
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

        }

        composable<IORoutes.ExportConfig> {
            Text("Export config screen.")
        }
    }
}