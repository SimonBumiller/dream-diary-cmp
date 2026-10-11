package me.ddcmp.feature.settings.entries.order_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test.database
import dream_diary_cmp.feature.settings.generated.resources.Res
import dream_diary_cmp.feature.settings.generated.resources.order_detail_backup_count
import dream_diary_cmp.feature.settings.generated.resources.order_detail_filetype
import dream_diary_cmp.feature.settings.generated.resources.order_detail_keeplast
import dream_diary_cmp.feature.settings.generated.resources.order_detail_location
import dream_diary_cmp.feature.settings.generated.resources.order_detail_next_backup
import dream_diary_cmp.feature.settings.generated.resources.order_detail_schedule
import dream_diary_cmp.feature.settings.generated.resources.order_detail_title
import dream_diary_cmp.feature.settings.generated.resources.order_detail_used_storage
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupsForOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupsForOrderUseCase.BackupsForOrder
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.format.formatDataAmount
import me.bumiller.ddcmp.ui.common.format.formatInstant
import me.bumiller.ddcmp.ui.common.format.formatInstantDayTime
import me.bumiller.ddcmp.ui.common.icons.file_export
import me.bumiller.ddcmp.ui.common.plus
import me.ddcmp.feature.settings.entries.order_detail.OrderDetailViewModel.UiEvent
import me.ddcmp.feature.settings.entries.order_detail.OrderDetailViewModel.ViewModelEvent
import me.ddcmp.feature.settings.entries.order_detail.model.OrderState
import me.ddcmp.feature.settings.entries.order_detail.model.state
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OrderDetailEntry(
    onGoBack: () -> Unit,
    onFreshSetup: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, OrderDetailViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.NavigateBack -> onGoBack()
                ViewModelEvent.NavigateToFreshSetup -> onFreshSetup()
            } }
    ) { vm ->
        val order by vm.order.collectAsStateWithLifecycle()
        val orderBackups by vm.orderBackups.collectAsStateWithLifecycle()

        if(order != null && orderBackups != null) {
            OrderDetailEntry(order!!, orderBackups!!, vm::onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun OrderDetailEntry(
    order: PeriodicBackupOrder,
    orderBackups: BackupsForOrder,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.order_detail_title)) },
                navigationIcon = { BackIconButton { onEvent(UiEvent.ClickBack) } },
                actions = {
                    order.state().positiveActionLabelAndIcon()?.let { (vector, _) ->
                        FilledIconButton(
                            onClick = { onEvent(UiEvent.TriggerPositiveAction) },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .size(IconButtonDefaults.extraSmallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide))
                        ) {
                            Icon(
                                imageVector = vector,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(IconButtonDefaults.extraSmallIconSize)
                            )
                        }
                    }
                }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .padding(pv + PaddingValues(horizontal = 16.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OrderStateSection(
                modifier = Modifier
                    .fillMaxWidth(),
                order = order
            )

            ActionsSection(order, onEvent)

            when (order.state()) {
                OrderState.Saved -> SavedOrderInfo(
                    modifier = Modifier
                        .fillMaxWidth(),
                    order = order
                )
                OrderState.Running, OrderState.Canceled -> RunningOrderInfo(
                    modifier = Modifier
                        .fillMaxWidth(),
                    order = order,
                    orderBackups = orderBackups
                )
            }

        }
    }
}

@Composable
fun RunningOrderInfo(
    modifier: Modifier,
    order: PeriodicBackupOrder,
    orderBackups: BackupsForOrder
) {
    Column(
        modifier = modifier
    ) {
        InfoItem(
            icon = database,
            label = stringResource(Res.string.order_detail_backup_count),
            value = orderBackups.backups.size.translateBackupCount()
        )
        HorizontalDivider()
        InfoItem(
            icon = Icons.Outlined.Storage,
            label = stringResource(Res.string.order_detail_used_storage),
            value = orderBackups.totalSize.formatDataAmount()
        )
        HorizontalDivider()
        InfoItem(
            icon = file_export,
            label = stringResource(Res.string.order_detail_next_backup),
            value = orderBackups.mostRecentBackup?.created?.plus(order.period)
                ?.let(::formatInstantDayTime) ?: ""
        )
//        HorizontalDivider()
//        InfoItem(
//            icon = database,
//            label = stringResource(Res.string.order_detail_keeplast),
//            value = order.keepLast.translateKeepLast()
//        )
    }
}

@Composable
private fun SavedOrderInfo(
    modifier: Modifier,
    order: PeriodicBackupOrder
) {
    Column(
        modifier = modifier
    ) {
        InfoItem(
            icon = Icons.Outlined.FolderOpen,
            label = stringResource(Res.string.order_detail_location),
            value = order.destination.toString()
        )
        HorizontalDivider()
        InfoItem(
            icon = Icons.Outlined.Schedule,
            label = stringResource(Res.string.order_detail_schedule),
            value = order.translateSchedule()
        )
        HorizontalDivider()
        InfoItem(
            icon = file_export,
            label = stringResource(Res.string.order_detail_filetype),
            value = order.exportOption.translateFileType()
        )
        HorizontalDivider()
        InfoItem(
            icon = database,
            label = stringResource(Res.string.order_detail_keeplast),
            value = order.keepLast.translateKeepLast()
        )
    }
}

@Composable
private fun ActionsSection(
    order: PeriodicBackupOrder,
    onEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        order.state().positiveActionLabelAndIcon()?.let { (icon, label) ->
            Button(
                modifier = Modifier
                    .width(300.dp),
                onClick = { onEvent(UiEvent.TriggerPositiveAction) }
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    imageVector = icon,
                    contentDescription = null
                )

                Text(stringResource(label))
            }
        }
        order.state().negativeActionLabelAndIcon()?.let { (icon, label) ->
            OutlinedButton(
                modifier = Modifier
                    .width(300.dp),
                onClick = { onEvent(UiEvent.TriggerNegativeAction) },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
                border = ButtonDefaults.outlinedButtonBorder().copy(
                    brush = SolidColor(MaterialTheme.colorScheme.error),
                ),
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    imageVector = icon,
                    contentDescription = null
                )

                Text(stringResource(label))
            }
        }
    }
}

@Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    ListItem(
        modifier = modifier,
        leadingContent = { Icon(imageVector = icon, contentDescription = null) },
        headlineContent = { Text(label) },
        supportingContent = {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis
            )
        }
    )
}

@Composable
private fun OrderStateSection(
    modifier: Modifier,
    order: PeriodicBackupOrder
) {
    val icon = order.state().icon()
    val (description, title) = order.state().translateDescriptionAndTitle()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(128.dp),
            imageVector = icon,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}