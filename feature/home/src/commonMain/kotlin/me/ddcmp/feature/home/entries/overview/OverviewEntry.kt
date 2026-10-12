package me.ddcmp.feature.home.entries.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.overview_actions_settings
import dream_diary_cmp.feature.home.generated.resources.overview_actions_tags
import dream_diary_cmp.feature.home.generated.resources.overview_dreams_label
import dream_diary_cmp.feature.home.generated.resources.overview_feature_dreams_title
import dream_diary_cmp.feature.home.generated.resources.overview_feature_emotions_title
import dream_diary_cmp.feature.home.generated.resources.overview_feature_persons_title
import dream_diary_cmp.feature.home.generated.resources.overview_no_recent_dreams
import dream_diary_cmp.feature.home.generated.resources.overview_record_dream
import dream_diary_cmp.feature.home.generated.resources.overview_title
import me.bumiller.ddcmp.domain.model.stats.RelativeRelation
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.layout.AppBarAction
import me.bumiller.ddcmp.ui.common.layout.AppBarActions
import me.bumiller.ddcmp.ui.common.plus
import me.ddcmp.feature.home.component.DreamLazyColumn
import me.ddcmp.feature.home.component.OverviewCurrentWeekCard
import me.ddcmp.feature.home.component.OverviewFeatureCard
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.UiEvent
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OverviewEntry(
    onAddDream: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToEmotions: () -> Unit,
    onGoToStats: () -> Unit,
    onGoToPersons: () -> Unit,
    onGoToDreams: () -> Unit,
    onGoToTags: () -> Unit,
    onGoToDreamDetail: (id: Long) -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, OverviewViewModel>(
        onViewModelEvent = {
            when (it) {
                is ViewModelEvent.GoToDream -> onGoToDreamDetail(it.dreamId)
                ViewModelEvent.GoToDreams -> onGoToDreams()
                ViewModelEvent.GoToEmotions -> onGoToEmotions()
                ViewModelEvent.GoToPersons -> onGoToPersons()
                ViewModelEvent.GoToSettings -> onGoToSettings()
                ViewModelEvent.GoToStatistics -> onGoToStats()
                ViewModelEvent.AddDream -> onAddDream()
                ViewModelEvent.GoToTags -> onGoToTags()
            }
        }
    ) { vm ->
        val recentDreams by vm.recentDreams.collectAsStateWithLifecycle()
        val dreamCount by vm.dreamCount.collectAsStateWithLifecycle()
        val emotionCount by vm.emotionCount.collectAsStateWithLifecycle()
        val personCount by vm.personCount.collectAsStateWithLifecycle()
        val recentDreamActivity by vm.recentDreamActivity.collectAsStateWithLifecycle()

        val allNotNull = listOf(recentDreams, dreamCount, emotionCount, personCount)
            .all { it != null }

        if (allNotNull) {
            OverviewEntry(
                onEvent = vm::onEvent,
                recentDreams = recentDreams!!,
                dreamCount = dreamCount!!,
                emotionCount = emotionCount!!,
                personCount = personCount!!,
                recentDreamActivity = recentDreamActivity ?: Stat.NotEnoughData()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OverviewEntry(
    onEvent: (UiEvent) -> Unit,
    recentDreams: List<AllDreamUseCase.O>,
    dreamCount: Int,
    emotionCount: Int,
    personCount: Int,
    recentDreamActivity: Stat<RelativeRelation>
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // TODO: Possibly replace this with a stylized logo of text
                    Text(text = stringResource(Res.string.overview_title))
                },
                actions = {
                    AppBarActions(
                        actions = listOf(
                            AppBarAction(
                                label = stringResource(Res.string.overview_actions_settings),
                                icon = Icons.Default.Settings,
                                handler = { onEvent(UiEvent.GoToSettings) }
                            ),
                            AppBarAction(
                                label = stringResource(Res.string.overview_actions_tags),
                                icon = Icons.AutoMirrored.Filled.Label,
                                handler = { onEvent(UiEvent.GoToTags) }
                            )
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(Res.string.overview_record_dream)) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                },
                onClick = { onEvent(UiEvent.AddDream) }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv + PaddingValues(horizontal = 8.dp))
        ) {
            OverviewCurrentWeekCard(
                modifier = Modifier
                    .fillMaxWidth(),
                currentWeekRelative = recentDreamActivity,
                onClick = { onEvent(UiEvent.GoToStatistics) }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OverviewFeatureCard(
                    modifier = Modifier
                        .weight(1F),
                    name = stringResource(Res.string.overview_feature_persons_title),
                    count = personCount,
                    icon = Icons.Default.Person,
                    onClick = { onEvent(UiEvent.GoToPersons) },
                    containerColor = MaterialTheme.colorScheme.primary
                )
                OverviewFeatureCard(
                    modifier = Modifier
                        .weight(1F),
                    name = stringResource(Res.string.overview_feature_emotions_title),
                    count = emotionCount,
                    icon = Icons.Default.Favorite,
                    onClick = { onEvent(UiEvent.GoToEmotions) },
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }

            OverviewFeatureCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                name = stringResource(Res.string.overview_feature_dreams_title),
                count = dreamCount,
                icon = Icons.AutoMirrored.Outlined.Article,
                onClick = { onEvent(UiEvent.GoToDreams) },
                containerColor = MaterialTheme.colorScheme.tertiary
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            Text(
                text = stringResource(Res.string.overview_dreams_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            DreamLazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                dreams = recentDreams,
                onClick = { onEvent(UiEvent.GoToDream(it.id)) },
                emptyPlaceholder = {
                    stringResource(Res.string.overview_no_recent_dreams)
                }
            )
        }
    }
}