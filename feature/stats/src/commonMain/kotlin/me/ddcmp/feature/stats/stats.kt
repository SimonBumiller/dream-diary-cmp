package me.ddcmp.feature.stats

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.ddcmp.feature.stats.entries.dream.DreamEntry
import me.ddcmp.feature.stats.entries.dreamglobal.DreamGlobal
import me.ddcmp.feature.stats.navigation.StatsRoutes

/**
 * Navigates to the global stats route.
 */
public fun NavController.navigateToGlobalStats(): Unit =
    navigate(StatsRoutes.Global)

/**
 * Navigates to the global stats route.
 *
 * @param dreamId The id of the dream.
 */
public fun NavController.navigateToDreamStats(dreamId: Long): Unit =
    navigate(StatsRoutes.Dream(dreamId))

/**
 * Navigation routes for the global statistics
 */
public fun NavGraphBuilder.stats(
    onGoToPerson: (Long) -> Unit,
    navController: NavController
) {
    composable<StatsRoutes.Global> {
        DreamGlobal(
            onReturn = { navController.popBackStack() },
            onGoToPerson = onGoToPerson
        )
    }

    composable<StatsRoutes.Dream> {
        DreamEntry(
            onBack = { navController.popBackStack() }
        )
    }
}