package me.ddcmp.feature.person

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import me.ddcmp.feature.person.entries.personadd.PersonAddEntry
import me.ddcmp.feature.person.entries.persondetail.PersonDetailEntry
import me.ddcmp.feature.person.entries.personlist.PersonListEntry
import me.ddcmp.feature.person.navigation.PersonRoutes

/**
 * Navigates to the person list route.
 */
public fun NavController.navigateToPersonList(): Unit =
    navigate(PersonRoutes.PersonList)

/**
 * Navigates to the person detail route.
 *
 * @param personId The id of the person.
 */
public fun NavController.navigateToPersonDetail(personId: Long): Unit =
    navigate(PersonRoutes.PersonDetail(personId))

/**
 * Navigates to the person add route.
 *
 * @param personId The id of the person to edit, or null to create a new one.
 */
public fun NavController.navigateToAddPerson(personId: Long? = null): Unit =
    navigate(PersonRoutes.AddPerson(personId))

/**
 * Adds the person destinations to the navigation graph.
 */
public fun NavGraphBuilder.person(
    goToDreamDetail: (Long, Boolean) -> Unit,
    navController: NavController
) {
    composable<PersonRoutes.PersonList> {
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
    composable<PersonRoutes.PersonDetail> {
        PersonDetailEntry(
            onBack = { navController.popBackStack() },
            onEdit = navController::navigateToAddPerson,
            onGoToDream = goToDreamDetail,
            onGoToPerson = {
                navController.navigate(PersonRoutes.PersonDetail(it))
            },
            onGoToEmotion = {},
        )
    }

    dialog<PersonRoutes.AddPerson> {
        PersonAddEntry(
            onCloseDialog = {
                navController.popBackStack()
            }
        )
    }
}