package me.ddcmp.feature.import

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.navigationevent.OnForwardCompletedFallback
import me.bumiller.ddcmp.domain.model.ImportResult
import me.ddcmp.feature.import.entries.configuration.ConfigurationEntry
import me.ddcmp.feature.import.entries.result.ResultEntry
import me.ddcmp.feature.import.navigation.ImportRoutes
import kotlin.reflect.typeOf

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
        startDestination = ImportRoutes.Configuration
    ) {
        composable<ImportRoutes.Result> {
            ResultEntry(
                onContinue = { wasSuccess ->
                    onFinished(wasSuccess)
                }
            )
        }

        composable<ImportRoutes.Configuration> {
            ConfigurationEntry(
                onImport = { resultId ->
                    navController.navigate(ImportRoutes.Result(resultId))
                },
                onReturn = {
                    onFinished(false)
                }
            )
        }
    }
}