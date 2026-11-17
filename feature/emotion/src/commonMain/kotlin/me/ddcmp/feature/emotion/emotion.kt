package me.ddcmp.feature.emotion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.ddcmp.feature.emotion.navigation.EmotionRoutes
import me.ddcmp.feature.emotion.entries.emotionadd.EmotionAddEntry
import me.ddcmp.feature.emotion.entries.emotionlist.EmotionListEntry

/**
 * Navigates to the emotion list route.
 */
public fun NavController.navigateToEmotionList(): Unit =
    navigate(EmotionRoutes.EmotionList)

/**
 * Navigates to the emotion add route.
 *
 * @param emotionId The id of the emotion to edit, or null to create a new one.
 */
public fun NavController.navigateToAddEmotion(emotionId: Long? = null): Unit =
    navigate(EmotionRoutes.AddEmotion(emotionId))

/**
 * Adds the emotion destinations to the navigation graph.
 */
public fun NavGraphBuilder.emotion(
    navController: NavController
) {
    composable<EmotionRoutes.EmotionList> {
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

    composable<EmotionRoutes.AddEmotion> {
        EmotionAddEntry(
            onBack = {
                navController.popBackStack()
            }
        )
    }
}