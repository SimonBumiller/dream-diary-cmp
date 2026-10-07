package me.ddcmp.feature.export

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import me.ddcmp.feature.export.navigation.ExportRoutes

@Composable
fun ExportEntry(
    onBack: () -> Unit,
    onFinished: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ExportRoutes.Configuration
    ) {
        dialog<ExportRoutes.Result> {

        }

        composable<ExportRoutes.Configuration> {
            Text("Export config screen.")
        }
    }
}