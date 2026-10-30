package me.ddcmp.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import me.bumiller.ddcmp.ui.common.biometric.navigateSecured
import me.bumiller.ddcmp.ui.common.biometric.securableComposable
import me.ddcmp.feature.home.entries.dreamadd.DreamAddEntry
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailEntry
import me.ddcmp.feature.home.entries.dreamlist.DreamListEntry
import me.ddcmp.feature.home.entries.emotionadd.EmotionAddEntry
import me.ddcmp.feature.home.entries.emotionlist.EmotionListEntry
import me.ddcmp.feature.home.entries.overview.OverviewEntry
import me.ddcmp.feature.home.entries.personadd.PersonAddEntry
import me.ddcmp.feature.home.entries.persondetail.PersonDetailEntry
import me.ddcmp.feature.home.entries.personlist.PersonListEntry
import me.ddcmp.feature.home.entries.tagadd.TagAddEntry
import me.ddcmp.feature.home.entries.taglist.TagListEntry
import me.ddcmp.feature.home.navigation.HomeRoutes
import me.ddcmp.feature.stats.navigateToDreamStats
import me.ddcmp.feature.stats.navigateToGlobalStats
import me.ddcmp.feature.stats.stats

/**
 * Navigates to the overview route.
 */
public fun NavController.navigateToOverview(): Unit =
    navigate(HomeRoutes.Overview)

/**
 * Navigates to the dream list route.
 */
public fun NavController.navigateToDreamList(): Unit =
    navigate(HomeRoutes.DreamList)

/**
 * Navigates to the dream add route.
 *
 * @param dreamId The id of the dream to edit, or null to create a new one.
 */
public fun NavController.navigateToAddDream(dreamId: Long? = null): Unit =
    navigate(HomeRoutes.AddDream(dreamId))

/**
 * Navigates to the dream detail route.
 *
 * @param dreamId The id of the dream.
 * @param secured Whether the navigation should be secured with biometric authentication.
 */
public fun NavController.navigateToDreamDetail(dreamId: Long, secured: Boolean = true): Unit =
    navigateSecured(HomeRoutes.DreamDetail(dreamId), secured)

/**
 * Navigates to the emotion list route.
 */
public fun NavController.navigateToEmotionList(): Unit =
    navigate(HomeRoutes.EmotionList)

/**
 * Navigates to the emotion add route.
 *
 * @param emotionId The id of the emotion to edit, or null to create a new one.
 */
public fun NavController.navigateToAddEmotion(emotionId: Long? = null): Unit =
    navigate(HomeRoutes.AddEmotion(emotionId))

/**
 * Navigates to the person list route.
 */
public fun NavController.navigateToPersonList(): Unit =
    navigate(HomeRoutes.PersonList)

/**
 * Navigates to the person detail route.
 *
 * @param personId The id of the person.
 */
public fun NavController.navigateToPersonDetail(personId: Long): Unit =
    navigate(HomeRoutes.PersonDetail(personId))

/**
 * Navigates to the person add route.
 *
 * @param personId The id of the person to edit, or null to create a new one.
 */
public fun NavController.navigateToAddPerson(personId: Long? = null): Unit =
    navigate(HomeRoutes.AddPerson(personId))

/**
 * Navigates to the tag list route.
 */
public fun NavController.navigateToTagList(): Unit =
    navigate(HomeRoutes.TagList)

/**
 * Navigates to the tag add route.
 *
 * @param tagId The id of the tag to edit, or null to create a new one.
 */
public fun NavController.navigateToAddTag(tagId: Long? = null): Unit =
    navigate(HomeRoutes.AddTag(tagId))

/**
 * Adds the home destinations to the navigation graph.
 */
public fun NavGraphBuilder.home(
    onGoToSettings: () -> Unit,
    onLoaded: () -> Unit,
    navController: NavController
) {
    composable<HomeRoutes.DreamList> {
        DreamListEntry(
            onAddDream = navController::navigateToAddDream,
            onGoToDreamDetail = navController::navigateToDreamDetail,
            goBack = {
                navController.popBackStack()
            }
        )
    }
    composable<HomeRoutes.AddDream> {
        DreamAddEntry(
            onBack = {
                navController.popBackStack()
            }
        )
    }

    stats(
        onGoToPerson = {
            navController.navigate(HomeRoutes.PersonDetail(it))
        },
        navController = navController
    )

    composable<HomeRoutes.EmotionList> {
        EmotionListEntry(
            onBack = {
                navController.popBackStack()
            },
            onAdd = navController::navigateToAddEmotion,
            onEdit = {
                navController.navigateToAddEmotion(it)
            }
        )
    }
    composable<HomeRoutes.PersonList> {
        PersonListEntry(
            onBack = {
                navController.popBackStack()
            },
            onAdd = navController::navigateToAddPerson,
            onEdit = {
                navController.navigateToAddPerson(it)
            },
            onGoToPerson = navController::navigateToPersonDetail
        )
    }
    composable<HomeRoutes.TagList> {
        TagListEntry(
            onBack = {
                navController.popBackStack()
            },
            onAdd = navController::navigateToAddTag,
            onEdit = {
                navController.navigateToAddTag(it)
            }
        )
    }
    composable<HomeRoutes.AddEmotion> {
        EmotionAddEntry(
            onBack = {
                navController.popBackStack()
            }
        )
    }
    securableComposable<HomeRoutes.DreamDetail>(navController) {
        DreamDetailEntry(
            onEdit = navController::navigateToAddDream,
            onGoToEmotion = {},
            onGoToPerson = navController::navigateToPersonDetail,
            onGoToDreamStats = navController::navigateToDreamStats,
            onBack = { navController.popBackStack() }
        )
    }
    composable<HomeRoutes.PersonDetail> {
        PersonDetailEntry(
            onBack = { navController.popBackStack() },
            onEdit = navController::navigateToAddPerson,
            onGoToDream = navController::navigateToDreamDetail,
            onGoToPerson = {
                navController.navigate(HomeRoutes.PersonDetail(it))
            },
            onGoToEmotion = {},
        )
    }
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

    dialog<HomeRoutes.AddPerson> {
        PersonAddEntry(
            onCloseDialog = {
                navController.popBackStack()
            }
        )
    }
    dialog<HomeRoutes.AddTag> {
        TagAddEntry(
            onCloseDialog = {
                navController.popBackStack()
            }
        )
    }
}