package me.ddcmp.feature.stats

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.ddcmp.feature.stats.entries.DreamGlobal
import me.ddcmp.feature.stats.navigation.StatsRoutes.DreamGlobal

@Composable
fun StatsEntry(
    onReturn: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DreamGlobal
    ) {
        composable<DreamGlobal> {
            DreamGlobal(
                onReturn = onReturn
            )
        }
    }
}