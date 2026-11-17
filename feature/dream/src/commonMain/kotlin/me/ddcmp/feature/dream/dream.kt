package me.ddcmp.feature.dream

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.bumiller.ddcmp.ui.common.biometric.navigateSecured
import me.bumiller.ddcmp.ui.common.biometric.securableComposable
import me.ddcmp.feature.dream.navigation.DreamRoutes
import me.ddcmp.feature.dream.entries.dreamadd.DreamAddEntry
import me.ddcmp.feature.dream.entries.dreamdetail.DreamDetailEntry
import me.ddcmp.feature.dream.entries.dreamlist.DreamListEntry
import me.ddcmp.feature.stats.navigateToDreamStats

/**
 * Navigates to the dream list route.
 */
public fun NavController.navigateToDreamList(): Unit =
    navigate(DreamRoutes.DreamList)

/**
 * Navigates to the dream add route.
 *
 * @param dreamId The id of the dream to edit, or null to create a new one.
 */
public fun NavController.navigateToAddDream(dreamId: Long? = null): Unit =
    navigate(DreamRoutes.AddDream(dreamId))

/**
 * Navigates to the dream detail route.
 *
 * @param dreamId The id of the dream.
 * @param secured Whether the navigation should be secured with biometric authentication.
 */
public fun NavController.navigateToDreamDetail(dreamId: Long, secured: Boolean = true): Unit =
    navigateSecured(DreamRoutes.DreamDetail(dreamId), secured)

/**
 * Adds the dream destinations to the navigation graph.
 */
public fun NavGraphBuilder.dream(
    goToPerson: (Long) -> Unit,
    navController: NavController
) {
    composable<DreamRoutes.DreamList> {
        DreamListEntry(
            onAddDream = navController::navigateToAddDream,
            onGoToDreamDetail = navController::navigateToDreamDetail,
            goBack = {
                navController.popBackStack()
            }
        )
    }
    composable<DreamRoutes.AddDream> {
        DreamAddEntry(
            onBack = {
                navController.popBackStack()
            }
        )
    }

    securableComposable<DreamRoutes.DreamDetail>(navController) {
        DreamDetailEntry(
            onEdit = navController::navigateToAddDream,
            onGoToEmotion = {},
            onGoToPerson = goToPerson,
            onGoToDreamStats = navController::navigateToDreamStats,
            onBack = { navController.popBackStack() }
        )
    }
}