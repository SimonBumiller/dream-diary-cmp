package me.ddcmp.feature.settings.entries.order_config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dream_diary_cmp.feature.settings.generated.resources.Res
import dream_diary_cmp.feature.settings.generated.resources.order_config_description
import dream_diary_cmp.feature.settings.generated.resources.order_config_title
import dream_diary_cmp.feature.settings.generated.resources.order_config_wizard_retreat_label
import dream_diary_cmp.feature.settings.generated.resources.order_config_wizard_save_label
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.component.BottomRowScope
import me.bumiller.ddcmp.ui.common.component.DefaultBottomRow
import me.bumiller.ddcmp.ui.common.component.SetupWizard
import me.bumiller.ddcmp.ui.common.plus
import me.ddcmp.feature.settings.entries.order_config.model.OrderConfigWizardSteps
import org.jetbrains.compose.resources.stringResource

/**
 * Screen to create a backup order config.
 *
 * @param onCancel Invoked when the order creation is canceled
 * @param onConfigured Invoked when the order config was finished and saved, with its id.
 */
@Composable
internal fun OrderConfigEntry(
    onCancel: () -> Unit,
    onConfigured: (Long) -> Unit
) {
    OrderConfigEntry(
        onEvent = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderConfigEntry(
    onEvent: (Unit) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.order_config_title)) },
                navigationIcon = { BackIconButton { onEvent(Unit) } }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .padding(pv + PaddingValues(horizontal = 8.dp)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.order_config_description),
                style = MaterialTheme.typography.bodySmall
            )

            SetupWizard(
                modifier = Modifier
                    .fillMaxWidth(),
                steps = OrderConfigWizardSteps.entries,
                canAdvance = {
                    true // TODO
                },
                canRetreat = {
                    true // TODO
                },
                onFinish = { onEvent(Unit) },
                onCancel = { onEvent(Unit) },
                bottomRow = { OrderConfigWizardBottomRow(modifier = Modifier.fillMaxWidth()) }
            ) { step ->
                Box(
                    modifier = Modifier
                        .weight(1F)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                )
            }
        }

    }
}

@Composable
private fun BottomRowScope<OrderConfigWizardSteps>.OrderConfigWizardBottomRow(
    modifier: Modifier
) {
    if(isLast) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .width(300.dp),
                onClick = ::advance,
                enabled = canAdvance
            ) {
                Text(stringResource(Res.string.order_config_wizard_save_label))
            }

            OutlinedButton(
                modifier = Modifier
                    .width(300.dp),
                onClick = ::retreat,
                enabled = canRetreat
            ) {
                Text(stringResource(Res.string.order_config_wizard_retreat_label))
            }
        }
    } else {
        DefaultBottomRow(modifier)
    }
}