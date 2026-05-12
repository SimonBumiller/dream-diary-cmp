package me.ddcmp.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.ddcmp.feature.home.entries.dreamadd.DreamAddEntry
import me.ddcmp.feature.home.entries.dreamlist.DreamListEntry
import me.ddcmp.feature.home.entries.emotionlist.EmotionListEntry
import me.ddcmp.feature.home.navigation.HomeRoutes
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
                    onGoToStats = {},
                    onGoToSettings = {},
                    onGoToPersons = {}
                )
            }
            composable<HomeRoutes.AddDream> {
                DreamAddEntry(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable<HomeRoutes.EmotionList> {
                EmotionListEntry(
                    onBack = {
                        navController.popBackStack()
                    },
                    onAdd = {}
                )
            }
        }
    }
}