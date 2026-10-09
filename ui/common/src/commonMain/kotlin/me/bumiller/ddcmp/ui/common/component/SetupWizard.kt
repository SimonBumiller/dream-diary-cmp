package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.preview_wizard_first
import dream_diary_cmp.ui.common.generated.resources.preview_wizard_second
import dream_diary_cmp.ui.common.generated.resources.preview_wizard_third
import dream_diary_cmp.ui.common.generated.resources.wizard_row_advance_label
import dream_diary_cmp.ui.common.generated.resources.wizard_row_retreat_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Symbolizes a step in the wizard process.
 */
interface WizardStep {

    /**
     * The string resource for the step.
     */
    val nameRes: StringResource

}

/**
 * Default bottom row with two text buttons.
 */
@Composable
fun <Step : WizardStep> BottomRowScope<Step>.DefaultBottomRow(
    modifier: Modifier,
    retreatText: String = stringResource(Res.string.wizard_row_retreat_label),
    advanceText: String = stringResource(Res.string.wizard_row_advance_label),
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = ::retreat,
            enabled = canRetreat
        ) { Text(retreatText) }

        TextButton(
            onClick = ::advance,
            enabled = canAdvance
        ) { Text(advanceText) }
    }
}

/**
 * Scope for the bottom row composable.
 */
interface BottomRowScope<Step : WizardStep> {

    /**
     * The current step.
     */
    val current: Step

    /**
     * Whether the wizard can advance.
     */
    val canAdvance: Boolean

    /**
     * Whether the wizard can retreat.
     */
    val canRetreat: Boolean

    /**
     * Retreat to the recent step.
     */
    fun retreat()

    /**
     * Advance to the next step.
     */
    fun advance()

    /**
     * Whether it's the last step.
     */
    val isLast: Boolean

    /**
     * Whether it's the first step.
     */
    val isFirst: Boolean

}

/**
 * Composable for a multistep wizard.
 *
 * @param canAdvance Whether the specified step can advance
 * @param canRetreat Whether the specified step can retreat
 * @param onFinish Invoked when the last step is advanced
 * @param onCancel Invoked when the initial step is retreated
 */
@Composable
fun <Step : WizardStep> SetupWizard(
    modifier: Modifier,
    steps: List<Step>,
    canAdvance: (Step) -> Boolean = { true },
    canRetreat: (Step) -> Boolean = { true },
    onFinish: () -> Unit,
    onCancel: () -> Unit,
    bottomRow: @Composable BottomRowScope<Step>.() -> Unit = { DefaultBottomRow(modifier = Modifier.fillMaxWidth()) },
    content: @Composable ColumnScope.(Step) -> Unit
) {
    require(steps.size > 1) { "Must have at least 2 steps in a setup wizard." }

    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val onClickAction: (Int) -> Unit = { index ->
                val isBefore = index < selectedIndex
                val isAfter = index > selectedIndex

                if (isBefore) {
                    val allBetween = steps.subList(index + 1, selectedIndex)
                    val canAllRetreat = allBetween.all(canRetreat)
                    if (canAllRetreat) selectedIndex = index
                }
                if (isAfter) {
                    val allBetween = steps.subList(selectedIndex, index)
                    val canAllAdvance = allBetween.all(canAdvance)
                    if (canAllAdvance) selectedIndex = index
                }
            }

            steps.take(steps.size - 1).forEachIndexed { index, step ->
                WizardHeaderItem(
                    modifier = Modifier,
                    step = step,
                    onClick = { onClickAction(index) },
                    isSelected = index == selectedIndex,
                    isPrevious = index < selectedIndex
                )

                HorizontalDivider(
                    modifier = Modifier
                        .weight(1F)
                )
            }

            WizardHeaderItem(
                modifier = Modifier,
                step = steps.last(),
                onClick = { onClickAction(steps.indices.last()) },
                isSelected = steps.indices.last() == selectedIndex,
                isPrevious = steps.indices.last() < selectedIndex
            )
        }

        content(steps[selectedIndex])

        val bottomRowScope = object : BottomRowScope<Step> {
            override val current = steps[selectedIndex]
            override val canAdvance = canAdvance(current)
            override val canRetreat = canRetreat(current)

            override val isLast = selectedIndex == steps.indices.last
            override val isFirst = selectedIndex == 0

            override fun retreat() {
                if (!this.canRetreat) return
                if (isFirst) onCancel()
                else selectedIndex--
            }

            override fun advance() {
                if (!this.canAdvance) return
                if (isLast) onFinish()
                else selectedIndex++
            }
        }
        bottomRow(bottomRowScope)
    }
}

@Composable
private fun WizardHeaderItem(
    modifier: Modifier,
    step: WizardStep,
    onClick: () -> Unit,
    isSelected: Boolean,
    isPrevious: Boolean
) {
    val padding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    val shape = RoundedCornerShape(16.dp)

    val background =
        when (isSelected) {
            true -> MaterialTheme.colorScheme.primaryContainer
            false -> when (isPrevious) {
                true -> MaterialTheme.colorScheme.secondaryContainer
                false -> Color.Transparent
            }
        }
    val content = contentColorFor(background)

    val alpha = if (isPrevious || isSelected) 1F
    else 0.39F

    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .clickable(onClick = onClick)
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(step.nameRes),
            style = MaterialTheme.typography.labelLarge,
            color = content.copy(alpha = alpha)
        )
    }
}

enum class PreviewStep(
    override val nameRes: StringResource
) : WizardStep {

    First(Res.string.preview_wizard_first),

    Second(Res.string.preview_wizard_second),

    Third(Res.string.preview_wizard_third),

}

@Preview
@Composable
fun SetupWizardPreview() {
    SetupWizard(
        modifier = Modifier
            .width(400.dp),
        steps = PreviewStep.entries,
        onFinish = {},
        onCancel = {}
    ) { step ->
        when (step) {
            PreviewStep.First -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Blue)
            )

            PreviewStep.Second -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Red)
            )

            PreviewStep.Third -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Green)
            )
        }
    }
}