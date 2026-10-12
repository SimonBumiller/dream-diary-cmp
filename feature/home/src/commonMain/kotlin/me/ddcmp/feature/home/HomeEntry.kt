package me.ddcmp.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
import me.ddcmp.feature.stats.StatsEntry
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun HomeEntry(
    onGoToSettings: () -> Unit
) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeRoutes.Overview
        ) {
            composable<HomeRoutes.DreamList> {
                DreamListEntry(
                    onAddDream = {
                        navController.navigate(HomeRoutes.AddDream())
                    },
                    onGoToDreamDetail = {
                        navController.navigate(HomeRoutes.DreamDetail(it))
                    },
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
            composable<HomeRoutes.Statistics> {
                val route = it.savedStateHandle.toRoute<HomeRoutes.Statistics>()

                StatsEntry(
                    dreamId = route.dreamId,
                    onReturn = {
                        navController.popBackStack()
                    },
                    onGoToPerson = {
                        navController.navigate(HomeRoutes.PersonDetail(it))
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
                    },
                    onGoToPerson = {
                        navController.navigate(HomeRoutes.PersonDetail(it))
                    }
                )
            }
            composable<HomeRoutes.TagList> {
                TagListEntry(
                    onBack = {
                        navController.popBackStack()
                    },
                    onAdd = {
                        navController.navigate(HomeRoutes.AddTag())
                    },
                    onEdit = {
                        navController.navigate(HomeRoutes.AddTag(it))
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
                        navController.navigate(HomeRoutes.EmotionList)
                    },
                    onGoToPerson = {
                        navController.navigate(HomeRoutes.PersonDetail(it))
                    },
                    onGoToDreamStats = {
                        navController.navigate(HomeRoutes.Statistics(it))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<HomeRoutes.PersonDetail> {
                PersonDetailEntry(
                    onBack = { navController.popBackStack() },
                    onEdit = {
                        navController.navigate(HomeRoutes.AddPerson(it))
                    },
                    onGoToDream = {
                        navController.navigate(HomeRoutes.DreamDetail(it))
                    },
                    onGoToPerson = {
                        navController.navigate(HomeRoutes.PersonDetail(it))
                    },
                    onGoToEmotion = {
//                        navController.navigate(HomeRoutes.EmotionList(it))
                    },
                )
            }
            composable<HomeRoutes.Overview> {
                OverviewEntry(
                    onAddDream = {
                        navController.navigate(HomeRoutes.AddDream())
                    },
                    onGoToSettings = onGoToSettings,
                    onGoToEmotions = {
                        navController.navigate(HomeRoutes.EmotionList)
                    },
                    onGoToStats = {
                        navController.navigate(HomeRoutes.Statistics())
                    },
                    onGoToPersons = {
                        navController.navigate(HomeRoutes.PersonList)
                    },
                    onGoToDreams = {
                        navController.navigate(HomeRoutes.DreamList)
                    },
                    onGoToDreamDetail = {
                        navController.navigate(HomeRoutes.DreamDetail(it))
                    },
                    onGoToTags = {
                        navController.navigate(HomeRoutes.TagList)
                    }
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
    }
}