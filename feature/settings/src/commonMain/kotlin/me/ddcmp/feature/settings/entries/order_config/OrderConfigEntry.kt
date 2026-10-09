package me.ddcmp.feature.settings.entries.order_config

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.settings.generated.resources.Res
import dream_diary_cmp.feature.settings.generated.resources.order_config_description
import dream_diary_cmp.feature.settings.generated.resources.order_config_options_filetype
import dream_diary_cmp.feature.settings.generated.resources.order_config_options_keeplast
import dream_diary_cmp.feature.settings.generated.resources.order_config_options_keeplast_limited
import dream_diary_cmp.feature.settings.generated.resources.order_config_options_keeplast_unlimited
import dream_diary_cmp.feature.settings.generated.resources.order_config_options_type
import dream_diary_cmp.feature.settings.generated.resources.order_config_schedule_inital_deferred
import dream_diary_cmp.feature.settings.generated.resources.order_config_schedule_inital_immediately
import dream_diary_cmp.feature.settings.generated.resources.order_config_schedule_initial_description
import dream_diary_cmp.feature.settings.generated.resources.order_config_schedule_period_days_label
import dream_diary_cmp.feature.settings.generated.resources.order_config_schedule_period_description
import dream_diary_cmp.feature.settings.generated.resources.order_config_schedule_period_hours_label
import dream_diary_cmp.feature.settings.generated.resources.order_config_storage_description
import dream_diary_cmp.feature.settings.generated.resources.order_config_storage_description_important
import dream_diary_cmp.feature.settings.generated.resources.order_config_storage_directory_placeholder
import dream_diary_cmp.feature.settings.generated.resources.order_config_title
import dream_diary_cmp.feature.settings.generated.resources.order_config_wizard_retreat_label
import dream_diary_cmp.feature.settings.generated.resources.order_config_wizard_save_label
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.component.BottomRowScope
import me.bumiller.ddcmp.ui.common.component.DefaultBottomRow
import me.bumiller.ddcmp.ui.common.component.IOFileSelector
import me.bumiller.ddcmp.ui.common.component.IOOptionTypeSelection
import me.bumiller.ddcmp.ui.common.component.PersistableDirectoryTextField
import me.bumiller.ddcmp.ui.common.component.SetupWizard
import me.bumiller.ddcmp.ui.common.model.IOOptionType
import me.bumiller.ddcmp.ui.common.plus
import me.ddcmp.feature.settings.entries.order_config.OrderConfigViewModel.OrderState
import me.ddcmp.feature.settings.entries.order_config.OrderConfigViewModel.UiEvent
import me.ddcmp.feature.settings.entries.order_config.OrderConfigViewModel.UiState
import me.ddcmp.feature.settings.entries.order_config.OrderConfigViewModel.ViewModelEvent
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
    ViewModelScope<UiEvent, ViewModelEvent, OrderConfigViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.Cancelled -> onCancel()
                is ViewModelEvent.OrderSaved -> onConfigured(it.orderId)
            }
        }
    ) { vm ->
        val orderState by vm.orderState.collectAsStateWithLifecycle()
        val uiState by vm.uiState.collectAsStateWithLifecycle()

        OrderConfigEntry(orderState, uiState, vm::onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderConfigEntry(
    orderState: OrderState,
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.order_config_title)) },
                navigationIcon = { BackIconButton { onEvent(UiEvent.ClickCancel) } }
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
                    when (it) {
                        OrderConfigWizardSteps.Storage -> orderState.isStorageValid()
                        OrderConfigWizardSteps.Schedule -> orderState.isScheduleValid()
                        OrderConfigWizardSteps.Options -> orderState.isValid()
                    }
                },
                onFinish = { onEvent(UiEvent.ClickSaveOrder) },
                onCancel = { onEvent(UiEvent.ClickCancel) },
                bottomRow = { OrderConfigWizardBottomRow(modifier = Modifier.fillMaxWidth()) }
            ) { step ->
                when (step) {
                    OrderConfigWizardSteps.Storage -> StorageConfiguration(
                        modifier = Modifier.fillMaxSize(),
                        orderState = orderState,
                        onEvent = onEvent
                    )

                    OrderConfigWizardSteps.Schedule -> ScheduleConfiguration(
                        modifier = Modifier.fillMaxSize(),
                        orderState = orderState,
                        onEvent = onEvent
                    )

                    OrderConfigWizardSteps.Options -> OptionsConfiguration(
                        modifier = Modifier.fillMaxSize(),
                        orderState = orderState,
                        onEvent = onEvent,
                        uiState = uiState
                    )
                }
            }
        }

    }
}

@Composable
private fun OptionsConfiguration(
    modifier: Modifier,
    orderState: OrderState,
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(Res.string.order_config_options_keeplast),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            KeepLastSelection(
                modifier = Modifier,
                orderState = orderState,
                onEvent = onEvent
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(Res.string.order_config_options_type),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            IOOptionTypeSelection(
                modifier = Modifier,
                type = orderState.ioOption,
                onTypeChange = { onEvent(UiEvent.ChangeIOOption(it)) }
            )
        }

        if (orderState.ioOption == IOOptionType.Normal) {
            Column {
                Text(
                    text = stringResource(Res.string.order_config_options_filetype),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IOFileSelector(
                    showDropdown = uiState.showFiletypeDropdown,
                    toggleDropdownVisibility = { onEvent(UiEvent.ToggleFiletypeDropdownVisibility) },
                    onTypeSelected = { onEvent(UiEvent.ChangeIOFileType(it)) },
                    fileType = orderState.ioFileType
                )
            }
        }
    }
}

@Composable
private fun KeepLastSelection(
    modifier: Modifier,
    orderState: OrderState,
    onEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        LabeledRadio(
            label = stringResource(Res.string.order_config_options_keeplast_unlimited),
            selected = orderState.limitlessRetention,
            onClick = { onEvent(UiEvent.ChangeLimitlessRetention(true)) }
        )

        LabeledRadio(
            label = stringResource(Res.string.order_config_options_keeplast_limited),
            selected = !orderState.limitlessRetention,
            onClick = { onEvent(UiEvent.ChangeLimitlessRetention(false)) }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .width(64.dp),
                value = orderState.keepLast,
                onValueChange = { onEvent(UiEvent.ChangeKeepLast(it)) },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = !orderState.limitlessRetention
            )
        }
    }
}

@Composable
private fun ScheduleConfiguration(
    modifier: Modifier,
    orderState: OrderState,
    onEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.order_config_schedule_period_description),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            SchedulePeriodPicker(Modifier, orderState, onEvent)

            Text(
                text = stringResource(Res.string.order_config_schedule_initial_description),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            ScheduleInitialPicker(Modifier, orderState, onEvent)
        }
    }
}

@Composable
private fun ScheduleInitialPicker(
    modifier: Modifier,
    orderState: OrderState,
    onEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            LabeledRadio(
                label = stringResource(Res.string.order_config_schedule_inital_immediately),
                selected = orderState.firstBackupImmediately,
                onClick = { onEvent(UiEvent.ChangeFirstBackupImmediately(true)) }
            )

            LabeledRadio(
                label = stringResource(Res.string.order_config_schedule_inital_deferred),
                selected = !orderState.firstBackupImmediately,
                onClick = { onEvent(UiEvent.ChangeFirstBackupImmediately(false)) }
            )
        }
    }
}

@Composable
private fun LabeledRadio(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    trailing: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            interactionSource = interactionSource
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )

        trailing?.invoke()
    }
}

@Composable
private fun SchedulePeriodPicker(
    modifier: Modifier,
    orderState: OrderState,
    onEvent: (UiEvent) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .width(64.dp),
            value = orderState.scheduleDays?.toString() ?: "",
            onValueChange = { onEvent(UiEvent.ChangeScheduleDays(it.toIntOrNull())) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )

        Text(
            text = stringResource(Res.string.order_config_schedule_period_days_label),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier
                .width(4.dp)
        )

        OutlinedTextField(
            modifier = Modifier
                .width(64.dp),
            value = orderState.scheduleHours?.toString() ?: "",
            onValueChange = { onEvent(UiEvent.ChangeScheduleHours(it.toIntOrNull())) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )

        Text(
            text = stringResource(Res.string.order_config_schedule_period_hours_label),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun StorageConfiguration(
    modifier: Modifier,
    orderState: OrderState,
    onEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(Res.string.order_config_storage_description),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(Res.string.order_config_storage_description_important),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        PersistableDirectoryTextField(
            modifier = Modifier,
            file = orderState.backupDirectory,
            onFileChanged = { onEvent(UiEvent.ChangeBackupDirectory(it)) },
            placeholder = stringResource(Res.string.order_config_storage_directory_placeholder)
        )
    }
}

@Composable
private fun BottomRowScope<OrderConfigWizardSteps>.OrderConfigWizardBottomRow(
    modifier: Modifier
) {
    if (isLast) {
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