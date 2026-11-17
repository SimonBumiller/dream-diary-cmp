package me.ddcmp.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.ddcmp.feature.dream.navigateToAddDream
import me.ddcmp.feature.dream.navigateToDreamDetail
import me.ddcmp.feature.dream.navigateToDreamList
import me.ddcmp.feature.emotion.navigateToEmotionList
import me.ddcmp.feature.home.entries.overview.OverviewEntry
import me.ddcmp.feature.home.navigation.HomeRoutes
import me.ddcmp.feature.person.navigateToPersonDetail
import me.ddcmp.feature.person.navigateToPersonList
import me.ddcmp.feature.stats.navigateToGlobalStats
import me.ddcmp.feature.stats.stats
import me.ddcmp.feature.tag.navigateToTagList

/**
 * Navigates to the overview route.
 */
public fun NavController.navigateToOverview(): Unit =
    navigate(HomeRoutes.Overview)

/**
 * Adds the home destinations to the navigation graph.
 */
public fun NavGraphBuilder.home(
    onGoToSettings: () -> Unit,
    onLoaded: () -> Unit,
    navController: NavController
) {
    stats(
        onGoToPerson = navController::navigateToPersonDetail,
        navController = navController
    )

    composable<HomeRoutes.Overview> {
        OverviewEntry(
            onAddDream = navController::navigateToAddDream,
            onGoToSettings = onGoToSettings,
            onGoToEmotions = navController::navigateToEmotionList,
            onGoToStats = navController::navigateToGlobalStats,
            onGoToPersons = navController::navigateToPersonList,
            onGoToDreams = navController::navigateToDreamList,
            onGoToDreamDetail = navController::navigateToDreamDetail,
            onGoToTags = navController::navigateToTagList,
            onLoaded = onLoaded
        )
    }
}