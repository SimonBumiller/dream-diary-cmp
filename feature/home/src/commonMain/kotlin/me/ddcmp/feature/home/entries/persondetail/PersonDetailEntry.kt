@file:OptIn(ExperimentalTime::class)

package me.ddcmp.feature.home.entries.persondetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.person_detail_associated_emotions_description
import dream_diary_cmp.feature.home.generated.resources.person_detail_associated_emotions_title
import dream_diary_cmp.feature.home.generated.resources.person_detail_associated_index
import dream_diary_cmp.feature.home.generated.resources.person_detail_associated_percentage
import dream_diary_cmp.feature.home.generated.resources.person_detail_associated_persons_description
import dream_diary_cmp.feature.home.generated.resources.person_detail_associated_persons_title
import dream_diary_cmp.feature.home.generated.resources.person_detail_dream_occurence_label
import dream_diary_cmp.feature.home.generated.resources.person_detail_edit
import dream_diary_cmp.feature.home.generated.resources.person_detail_first_occurence_label
import dream_diary_cmp.feature.home.generated.resources.person_detail_last_occurence_label
import dream_diary_cmp.feature.home.generated.resources.person_detail_no_info_placeholder
import dream_diary_cmp.feature.home.generated.resources.person_detail_no_occurence
import dream_diary_cmp.feature.home.generated.resources.person_detail_time_frequency_description
import dream_diary_cmp.feature.home.generated.resources.person_detail_time_frequency_period
import dream_diary_cmp.feature.home.generated.resources.person_detail_time_frequency_period_label
import dream_diary_cmp.feature.home.generated.resources.person_detail_time_frequency_title
import dream_diary_cmp.feature.home.generated.resources.person_detail_title
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.stats.Histogram
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamConditionalOccurrenceStatistic.ConditionalOccurrenceInfo
import me.bumiller.ddcmp.domain.stats.person.PersonOccurrenceStatistic.PersonOccurrenceInfo
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.AppTagInputChip
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.component.StatCard
import me.bumiller.ddcmp.ui.common.format.formatInstant
import me.bumiller.ddcmp.ui.common.format.formatLocalDate
import me.bumiller.ddcmp.ui.common.layout.AppBarAction
import me.bumiller.ddcmp.ui.common.layout.AppBarActions
import me.bumiller.ddcmp.ui.common.layout.TopBarWithProgressIndicator
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.bumiller.ddcmp.ui.diagram.histogram.Histogram
import me.bumiller.ddcmp.ui.diagram.histogram.HistogramLabelStyle
import me.ddcmp.feature.home.component.DreamLazyColumn
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.UiEvent
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.UiEvent.ChangeTab
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.UiEvent.GoBack
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.UiState
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.ViewModelEvent
import me.ddcmp.feature.stats.components.TimeFrequencyTooltip
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

@Composable
internal fun PersonDetailEntry(
    onBack: () -> Unit,
    onEdit: (id: Long) -> Unit,
    onGoToDream: (Long) -> Unit,
    onGoToPerson: (Long) -> Unit,
    onGoToEmotion: (Long) -> Unit,
) {
    val tabPagerState = rememberPagerState { PersonDetailTabs.entries.size }


    ViewModelScope<UiEvent, ViewModelEvent, PersonDetailViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.GoBack -> onBack()
                is ViewModelEvent.Edit -> onEdit(it.personId)
                is ViewModelEvent.GoToDream -> onGoToDream(it.dreamId)
                is ViewModelEvent.GoToEmotion -> onGoToEmotion(it.emotionId)
                is ViewModelEvent.GoToPerson -> onGoToPerson(it.personId)
                is ViewModelEvent.GoToPage -> {
                    tabPagerState.animateScrollToPage(it.page)
                }
            }
        }
    ) { vm ->

        LaunchedEffect(Unit) {
            snapshotFlow { tabPagerState.currentPage }
                .distinctUntilChanged()
                .collect { page ->
                    val tab = PersonDetailTabs.entries.find { it.index == page } ?: return@collect
                    vm.onEvent(ChangeTab(tab))
                }
        }

        val personState by vm.personState.collectAsStateWithLifecycle()
        val associatedEmotions by vm.associatedEmotions.collectAsStateWithLifecycle()
        val associatedPersons by vm.associatedPersons.collectAsStateWithLifecycle()
        val dreams by vm.dreamsState.collectAsStateWithLifecycle()
        val occurrence by vm.occurrenceStats.collectAsStateWithLifecycle()
        val timeFrequency by vm.timeFrequency.collectAsStateWithLifecycle()
        val uiState by vm.uiState.collectAsStateWithLifecycle()

        PersonDetailEntry(
            vm::onEvent,
            personState,
            associatedEmotions,
            associatedPersons,
            dreams,
            occurrence,
            timeFrequency,
            uiState,
            tabPagerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonDetailEntry(
    onEvent: (UiEvent) -> Unit,
    person: AllPersonUseCase.O?,
    associatedEmotions: Stat<List<ConditionalOccurrenceInfo<Emotion>>>?,
    associatedPersons: Stat<List<ConditionalOccurrenceInfo<Person>>>?,
    dreams: List<AllDreamUseCase.O>?,
    occurrence: Stat<PersonOccurrenceInfo>?,
    timeFrequency: Stat<Histogram<LocalDate, DatePeriod, Int>>?,
    uiState: UiState,
    tabPagerState: PagerState
) {
    val loading = person == null || occurrence == null

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithProgressIndicator(
                loading = loading
            ) {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(Res.string.person_detail_title)) },
                    navigationIcon = { BackIconButton { onEvent(GoBack) } },
                    actions = {
                        AppBarActions(
                            actions = listOf(AppBarAction(
                                label = stringResource(Res.string.person_detail_edit),
                                icon = Icons.Default.Edit,
                                handler = { onEvent(UiEvent.Edit) }
                            ))
                        )
                    }
                )
            }
        }
    ) { pv ->
        if (loading) return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv + PaddingValues(horizontal = 8.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Header(person)

            TabRow(
                selected = uiState.selectedTab,
                onChange = { onEvent(ChangeTab(it)) },
                dreamsAvailable = !dreams.isNullOrEmpty()
            )

            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize(),
                state = tabPagerState,
                verticalAlignment = Alignment.Top
            ) { page ->
                when (page) {
                    PersonDetailTabs.Stats.index -> StatisticsTab(
                        occurrence,
                        dreams,
                        associatedPersons,
                        person,
                        onEvent,
                        associatedEmotions,
                        timeFrequency
                    )

                    PersonDetailTabs.Dreams.index -> DreamsTab(dreams ?: emptyList(), onEvent)
                }
            }


        }
    }
}

@Composable
private fun StatisticsTab(
    occurrence: Stat<PersonOccurrenceInfo>,
    dreams: List<AllDreamUseCase.O>?,
    associatedPersons: Stat<List<ConditionalOccurrenceInfo<Person>>>?,
    person: AllPersonUseCase.O,
    onEvent: (UiEvent) -> Unit,
    associatedEmotions: Stat<List<ConditionalOccurrenceInfo<Emotion>>>?,
    timeFrequency: Stat<Histogram<LocalDate, DatePeriod, Int>>?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicStats(occurrence, dreams)

        if (associatedPersons != null) {
            StatCard(
                stat = associatedPersons,
                title = stringResource(Res.string.person_detail_associated_persons_title),
                description = stringResource(
                    Res.string.person_detail_associated_persons_description,
                    person.person.name
                )
            ) { persons ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    persons.forEachIndexed { index, person ->
                        AssociatedAttributeRow(
                            index = index + 1,
                            name = person.attribute.name,
                            percentage = person.percentageIncrease,
                            onClick = { onEvent(UiEvent.ClickPerson(person.attribute)) }
                        )
                    }
                }
            }
        }

        if (associatedEmotions != null) {
            StatCard(
                stat = associatedEmotions,
                title = stringResource(
                    Res.string.person_detail_associated_emotions_title,
                    person.person.name
                ),
                description = stringResource(
                    Res.string.person_detail_associated_emotions_description,
                    person.person.name
                )
            ) { emotion ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    emotion.forEachIndexed { index, emotion ->
                        AssociatedAttributeRow(
                            index = index + 1,
                            name = emotion.attribute.name,
                            percentage = emotion.percentageIncrease,
                            onClick = { onEvent(UiEvent.ClickEmotion(emotion.attribute)) }
                        )
                    }
                }
            }
        }

        if (timeFrequency != null) {
            StatCard(
                stat = timeFrequency,
                title = stringResource(Res.string.person_detail_time_frequency_title),
                description = stringResource(
                    Res.string.person_detail_time_frequency_description,
                    person.person.name
                )
            ) { histogram ->
                val maxPeriodStart = histogram
                    .dataMap
                    .entries
                    .maxByOrNull { it.value }
                    ?.let { if (it.value > histogram.average) it else null }
                    ?.key

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (maxPeriodStart != null) {
                        val maxPeriodEnd = maxPeriodStart.plus(histogram.bucketSize)

                        val maxPeriodString = formatLocalDate(maxPeriodStart)
                        val maxPeriodEndString = formatLocalDate(maxPeriodEnd)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.person_detail_time_frequency_period_label),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(
                                    Res.string.person_detail_time_frequency_period,
                                    maxPeriodString,
                                    maxPeriodEndString
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Histogram(
                        modifier = Modifier
                            .height(200.dp),
                        meta = histogram,
                        bucketLabel = { it.toString() },
                        bucketComparator = { date1, date2 ->
                            date1.toEpochDays().compareTo(date2.toEpochDays())
                        },
                        tooltip = { from, count, length ->
                            TimeFrequencyTooltip(
                                from = from,
                                length = length,
                                dreamCount = count
                            )
                        },
                        labelStyle = HistogramLabelStyle.PartiallyVisible(3)
                    )
                }
            }
        }
    }
}

@Composable
private fun DreamsTab(
    dreams: List<AllDreamUseCase.O>,
    onEvent: (UiEvent) -> Unit
) {
    DreamLazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        dreams = dreams,
        onClick = { onEvent(UiEvent.GoToDream(it.id)) }
    )
}

@Composable
private fun TabRow(
    selected: PersonDetailTabs,
    onChange: (PersonDetailTabs) -> Unit,
    dreamsAvailable: Boolean
) {
    SecondaryTabRow(
        selectedTabIndex = selected.index
    ) {
        PersonDetailTabs.entries.forEach { tab ->
            Tab(
                selected = tab == selected,
                text = { Text(stringResource(tab.textResource)) },
                onClick = { onChange(tab) },
                enabled = dreamsAvailable
            )
        }
    }
}

@Composable
private fun Header(
    person: AllPersonUseCase.O
) {
    Column {
        Text(
            text = person.person.name,
            style = MaterialTheme.typography.headlineMedium
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            person.tags.forEach { tag ->
                AppTagInputChip(
                    selected = false,
                    tag = tag,
                    onClick = {}
                )
            }
        }

        when (person.person.note) {
            null -> NoNote()
            else -> Note(person.person.note!!)
        }
    }
}

@Composable
private fun BasicStats(
    occurrence: Stat<PersonOccurrenceInfo>,
    dreams: List<AllDreamUseCase.O>?
) {
    Column {
        when (occurrence) {
            is Stat.NotEnoughData -> NoOccurrence()
            is Stat.Present -> Occurrence(occurrence.stat)
        }

        if (!dreams.isNullOrEmpty()) {
            DreamOccurrenceCount(dreams.size)
        }
    }
}

@Composable
private fun AssociatedAttributeRow(
    modifier: Modifier = Modifier,
    index: Int,
    name: String,
    percentage: Float,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.person_detail_associated_index, index),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.weight(1F))

        Text(
            text = stringResource(
                Res.string.person_detail_associated_percentage,
                (percentage * 100).roundToInt()
            ),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun Note(note: String) {
    Text(
        text = note,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun NoNote() {
    Text(
        text = stringResource(Res.string.person_detail_no_info_placeholder),
        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
    )
}

@Composable
private fun NoOccurrence() {
    DisabledContent {
        Text(
            text = stringResource(Res.string.person_detail_no_occurence),
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
        )
    }
}

@Composable
private fun DreamOccurrenceCount(count: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(Res.string.person_detail_dream_occurence_label),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun ColumnScope.Occurrence(occurrence: PersonOccurrenceInfo) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(Res.string.person_detail_first_occurence_label),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = formatInstant(occurrence.first),
            style = MaterialTheme.typography.titleMedium
        )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(Res.string.person_detail_last_occurence_label),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = formatInstant(occurrence.last),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
