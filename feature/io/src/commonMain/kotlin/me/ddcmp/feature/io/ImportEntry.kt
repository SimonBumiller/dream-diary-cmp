package me.ddcmp.feature.io

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.ddcmp.feature.io.entries.configuration.ImportConfigEntry
import me.ddcmp.feature.io.entries.result.ImportResultEntry
import me.ddcmp.feature.io.navigation.IORoutes

/**
 * Entry for the import section of the app.
 *
 * @param onFinished Called when the import procedure is finished. Result is null when the import action was aborted.
 */
@Composable
fun ImportEntry(
    onFinished: (finished: Boolean) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = IORoutes.ImportConfig
    ) {
        composable<IORoutes.ImportResult> {
            ImportResultEntry(
                onContinue = { wasSuccess ->
                    onFinished(wasSuccess)
                }
            )
        }

        composable<IORoutes.ImportConfig> {
            ImportConfigEntry(
                onImport = { resultId ->
                    navController.navigate(IORoutes.ImportResult(resultId))
                },
                onReturn = {
                    onFinished(false)
                }
            )
        }
    }
}