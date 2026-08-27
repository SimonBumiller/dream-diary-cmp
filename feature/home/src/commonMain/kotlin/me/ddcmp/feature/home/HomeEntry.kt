package me.ddcmp.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import me.ddcmp.feature.home.entries.dreamadd.DreamAddEntry
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailEntry
import me.ddcmp.feature.home.entries.dreamlist.DreamListEntry
import me.ddcmp.feature.home.entries.emotionadd.EmotionAddEntry
import me.ddcmp.feature.home.entries.emotionlist.EmotionListEntry
import me.ddcmp.feature.home.entries.personadd.PersonAddEntry
import me.ddcmp.feature.home.entries.personlist.PersonListEntry
import me.ddcmp.feature.home.navigation.HomeRoutes
import me.ddcmp.feature.stats.StatsEntry
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun HomeEntry() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeRoutes.DreamList
        ) {
            composable<HomeRoutes.DreamList> {
                DreamListEntry(
                    onAddDream = {
                        navController.navigate(HomeRoutes.AddDream())
                    },
                    onGoToEmotions = {
                        navController.navigate(HomeRoutes.EmotionList)
                    },
                    onGoToStats = {
                        navController.navigate(HomeRoutes.Statistics)
                    },
                    onGoToSettings = {},
                    onGoToPersons = {
                        navController.navigate(HomeRoutes.PersonList)
                    },
                    onGoToDreamDetail = {
                        navController.navigate(HomeRoutes.DreamDetail(it))
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
            composable<HomeRoutes.Statistics> {
                StatsEntry(
                    onReturn = {
                        navController.popBackStack()
                    }
                )
            }
            composable<HomeRoutes.EmotionList> {
                EmotionListEntry(
                    onBack = {
                        navController.popBackStack()
                    },
                    onAdd = {
                        navController.navigate(HomeRoutes.AddEmotion())
                    },
                    onEdit = {
                        navController.navigate(HomeRoutes.AddEmotion(it))
                    }
                )
            }
            composable<HomeRoutes.PersonList> {
                PersonListEntry(
                    onBack = {
                        navController.popBackStack()
                    },
                    onAdd = {
                        navController.navigate(HomeRoutes.AddPerson())
                    },
                    onEdit = {
                        navController.navigate(HomeRoutes.AddPerson(it))
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
            composable<HomeRoutes.DreamDetail> {
                DreamDetailEntry(
                    onEdit = {
                        navController.navigate(HomeRoutes.AddDream(it))
                    },
                    onGoToEmotion = {
                        // navController.navigate(HomeRoutes.Emot)
                    },
                    onGoToPerson = {
                        // navController.navigate(HomeRoutes.Perso)
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            dialog<HomeRoutes.AddPerson> {
                PersonAddEntry(
                    onCloseDialog = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}