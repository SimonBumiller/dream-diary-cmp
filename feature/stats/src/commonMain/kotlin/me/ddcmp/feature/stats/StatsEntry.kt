package me.ddcmp.feature.stats

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.ddcmp.feature.stats.entries.dream.DreamEntry
import me.ddcmp.feature.stats.entries.dreamglobal.DreamGlobal
import me.ddcmp.feature.stats.navigation.StatsRoutes
import me.ddcmp.feature.stats.navigation.StatsRoutes.DreamGlobal

@Composable
fun StatsEntry(
    dreamId: Long?,
    onReturn: () -> Unit,
    onGoToPerson: (Long) -> Unit
) {
    val navController = rememberNavController()

    val startDestination = if(dreamId != null) StatsRoutes.Dream(dreamId)
        else DreamGlobal

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<DreamGlobal> {
            DreamGlobal(
                onReturn = onReturn,
                onGoToPerson = onGoToPerson
            )
        }
        composable<StatsRoutes.Dream> {
            DreamEntry(
                onBack = onReturn
            )
        }
    }
}