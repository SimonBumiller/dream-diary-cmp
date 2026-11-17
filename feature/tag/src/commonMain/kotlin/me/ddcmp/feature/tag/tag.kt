package me.ddcmp.feature.tag

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import me.ddcmp.feature.tag.entries.tagadd.TagAddEntry
import me.ddcmp.feature.tag.entries.taglist.TagListEntry
import me.ddcmp.feature.tag.navigation.TagRoutes


/**
 * Navigates to the tag list route.
 */
public fun NavController.navigateToTagList(): Unit =
    navigate(TagRoutes.TagList)

/**
 * Navigates to the tag add route.
 *
 * @param tagId The id of the tag to edit, or null to create a new one.
 */
public fun NavController.navigateToAddTag(tagId: Long? = null): Unit =
    navigate(TagRoutes.AddTag(tagId))

/**
 * Adds the tag destinations to the navigation graph.
 */
public fun NavGraphBuilder.tag(
    navController: NavController
) {
    composable<TagRoutes.TagList> {
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

    dialog<TagRoutes.AddTag> {
        TagAddEntry(
            onCloseDialog = {
                navController.popBackStack()
            }
        )
    }
}