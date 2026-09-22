package me.ddcmp.feature.stats.entries.dream

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.stats.generated.resources.Res
import dream_diary_cmp.feature.stats.generated.resources.dream_length_description
import dream_diary_cmp.feature.stats.generated.resources.dream_length_info_center
import dream_diary_cmp.feature.stats.generated.resources.dream_length_info_length
import dream_diary_cmp.feature.stats.generated.resources.dream_length_info_post
import dream_diary_cmp.feature.stats.generated.resources.dream_length_info_pre
import dream_diary_cmp.feature.stats.generated.resources.dream_length_info_rank
import dream_diary_cmp.feature.stats.generated.resources.dream_length_title
import dream_diary_cmp.feature.stats.generated.resources.dream_pcount_description
import dream_diary_cmp.feature.stats.generated.resources.dream_pcount_info_center
import dream_diary_cmp.feature.stats.generated.resources.dream_pcount_info_pcount
import dream_diary_cmp.feature.stats.generated.resources.dream_pcount_info_post
import dream_diary_cmp.feature.stats.generated.resources.dream_pcount_info_pre
import dream_diary_cmp.feature.stats.generated.resources.dream_pcount_info_rank
import dream_diary_cmp.feature.stats.generated.resources.dream_pcount_title
import dream_diary_cmp.feature.stats.generated.resources.dream_title
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_length_average
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_length_high
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_length_highest
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_length_low
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_length_lowest
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_pcount_average
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_pcount_high
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_pcount_highest
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_pcount_low
import dream_diary_cmp.feature.stats.generated.resources.histogram_rank_label_pcount_lowest
import me.bumiller.ddcmp.domain.model.stats.HistogramRank
import me.bumiller.ddcmp.domain.model.stats.MarkedHistogram
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.MultiStyleText
import me.bumiller.ddcmp.ui.common.component.TextStyleWithCallback
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.diagram.histogram.Histogram
import me.bumiller.ddcmp.ui.diagram.histogram.HistogramLabelStyle
import me.ddcmp.feature.stats.components.DreamLengthTooltip
import me.ddcmp.feature.stats.components.DreamPersonCountTooltip
import me.ddcmp.feature.stats.components.HistogramBadge
import me.bumiller.ddcmp.ui.common.component.StatCard
import me.ddcmp.feature.stats.entries.dream.DreamViewModel.UiEvent
import me.ddcmp.feature.stats.entries.dream.DreamViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Composable
internal fun DreamEntry(
    onBack: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, DreamViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.OnBack -> onBack()
            }
        }
    ) { vm ->
        val dream by vm.dream.collectAsStateWithLifecycle()
        val lengthHistogram by vm.lengthHistogram.collectAsStateWithLifecycle()
        val personCountHistogram by vm.personCountHistogram.collectAsStateWithLifecycle()

        if (dream != null) {
            DreamEntry(vm::onEvent, dream!!, lengthHistogram, personCountHistogram)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DreamEntry(
    onEvent: (UiEvent) -> Unit,
    dream: AllDreamUseCase.O,
    lengthHistogram: Stat<MarkedHistogram<Int, Int, Int>>?,
    personCountHistogram: Stat<MarkedHistogram<Int, Unit, Int>>?
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(Res.string.dream_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(UiEvent.OnBack) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv + PaddingValues(horizontal = 8.dp))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (lengthHistogram != null) {
                LengthHistogram(lengthHistogram, dream)
            }

            if(personCountHistogram != null) {
                PersonCountHistogram(personCountHistogram, dream)
            }
        }
    }
}

@Composable
private fun LengthHistogram(
    lengthHistogram: Stat<MarkedHistogram<Int, Int, Int>>,
    dream: AllDreamUseCase.O
) {
    StatCard(
        stat = lengthHistogram,
        title = stringResource(Res.string.dream_length_title),
        description = stringResource(Res.string.dream_length_description),
        badges = { histogram ->
            HistogramBadge(
                rank = histogram.rank,
                label = { rank ->
                    val res = when (rank) {
                        HistogramRank.Lowest -> Res.string.histogram_rank_label_length_lowest
                        HistogramRank.Low -> Res.string.histogram_rank_label_length_low
                        HistogramRank.Average -> Res.string.histogram_rank_label_length_average
                        HistogramRank.High -> Res.string.histogram_rank_label_length_high
                        HistogramRank.Highest -> Res.string.histogram_rank_label_length_highest
                    }
                    stringResource(res)
                }
            )
        }
    ) { histogram ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Histogram(
                modifier = Modifier
                    .height(200.dp),
                meta = histogram.histogram,
                bucketLabel = { it.toString() },
                bucketComparator = { a, b -> a - b },
                tooltip = { from, count, _ ->
                    DreamLengthTooltip(
                        lowerLimit = from,
                        upperLimit = from + histogram.histogram.bucketSize,
                        dreamCount = count
                    )
                },
                markedBucket = histogram.markedBucket,
                labelStyle = HistogramLabelStyle.PartiallyVisible(5)
            )

            val defaultStyle = MaterialTheme.typography.bodyMedium
                .let { TextStyleWithCallback(it) }
            val highlightedText = MaterialTheme.typography.titleSmall
                .copy(fontWeight = FontWeight.Bold)
                .let { TextStyleWithCallback(it) }

            val textPairs = arrayOf(
                stringResource(Res.string.dream_length_info_pre) to defaultStyle,
                stringResource(
                    Res.string.dream_length_info_length,
                    dream.dream.length
                ) to highlightedText,
                stringResource(Res.string.dream_length_info_center) to defaultStyle,
                stringResource(
                    Res.string.dream_length_info_rank,
                    (histogram.rankPercentage * 100).roundToInt()
                ) to highlightedText,
                stringResource(Res.string.dream_length_info_post) to defaultStyle
            )
            MultiStyleText(
                defaultStyle = defaultStyle.style,
                pairs = textPairs
            )
        }
    }
}

@Composable
private fun PersonCountHistogram(
    personCountHistogram: Stat<MarkedHistogram<Int, Unit, Int>>,
    dream: AllDreamUseCase.O
) {
    StatCard(
        stat = personCountHistogram,
        title = stringResource(Res.string.dream_pcount_title),
        description = stringResource(Res.string.dream_pcount_description),
        badges = { histogram ->
            HistogramBadge(
                rank = histogram.rank,
                label = { rank ->
                    val res = when (rank) {
                        HistogramRank.Lowest -> Res.string.histogram_rank_label_pcount_lowest
                        HistogramRank.Low -> Res.string.histogram_rank_label_pcount_low
                        HistogramRank.Average -> Res.string.histogram_rank_label_pcount_average
                        HistogramRank.High -> Res.string.histogram_rank_label_pcount_high
                        HistogramRank.Highest -> Res.string.histogram_rank_label_pcount_highest
                    }
                    stringResource(res)
                }
            )
        }
    ) { histogram ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Histogram(
                modifier = Modifier
                    .height(200.dp),
                meta = histogram.histogram,
                bucketLabel = { it.toString() },
                bucketComparator = { a, b -> a - b },
                tooltip = { from, count, _ ->
                    DreamPersonCountTooltip(
                        dreamCount = count,
                        personCount = from
                    )
                },
                markedBucket = histogram.markedBucket,
                labelStyle = HistogramLabelStyle.PartiallyVisible(5)
            )

            val defaultStyle = MaterialTheme.typography.bodyMedium
                .let { TextStyleWithCallback(it) }
            val highlightedText = MaterialTheme.typography.titleSmall
                .copy(fontWeight = FontWeight.Bold)
                .let { TextStyleWithCallback(it) }

            val textPairs = arrayOf(
                stringResource(Res.string.dream_pcount_info_pre) to defaultStyle,
                stringResource(
                    Res.string.dream_pcount_info_pcount,
                    dream.persons.size
                ) to highlightedText,
                stringResource(Res.string.dream_pcount_info_center) to defaultStyle,
                stringResource(
                    Res.string.dream_pcount_info_rank,
                    (histogram.rankPercentage * 100).roundToInt()
                ) to highlightedText,
                stringResource(Res.string.dream_pcount_info_post) to defaultStyle
            )
            MultiStyleText(
                defaultStyle = defaultStyle.style,
                pairs = textPairs
            )
        }
    }
}