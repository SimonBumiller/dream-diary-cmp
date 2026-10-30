package me.ddcmp.feature.stats

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.ddcmp.feature.stats.entries.dream.DreamEntry
import me.ddcmp.feature.stats.entries.dreamglobal.DreamGlobal
import me.ddcmp.feature.stats.navigation.StatsRoutes

/**
 * Navigation routes for the global statistics
 */
public fun NavGraphBuilder.globalStats(
    onGoToPerson: (Long) -> Unit,
    navController: NavController
) {
    composable<StatsRoutes.Global> {
        DreamGlobal(
            onReturn = { navController.popBackStack() },
            onGoToPerson = onGoToPerson
        )
    }
}

/**
 * Navigation routes for the dream statistics.
 */
public fun NavGraphBuilder.dreamStats(
    navController: NavController
) {
    composable<StatsRoutes.Dream> {
        DreamEntry(
            onBack = { navController.popBackStack() }
        )
    }
}