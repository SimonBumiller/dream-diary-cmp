package me.ddcmp.feature.settings.entries.order_detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.UpdateDisabled
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.runtime.Composable
import dream_diary_cmp.feature.settings.generated.resources.Res
import dream_diary_cmp.feature.settings.generated.resources.order_detail_backup_count_none
import dream_diary_cmp.feature.settings.generated.resources.order_detail_filetype_legacy
import dream_diary_cmp.feature.settings.generated.resources.order_detail_keeplast_limited
import dream_diary_cmp.feature.settings.generated.resources.order_detail_keeplast_unlimited
import dream_diary_cmp.feature.settings.generated.resources.order_detail_schedule_value
import dream_diary_cmp.feature.settings.generated.resources.order_detail_schedule_value_days
import dream_diary_cmp.feature.settings.generated.resources.order_detail_schedule_value_hours
import dream_diary_cmp.feature.settings.generated.resources.order_detail_state_canceled_description
import dream_diary_cmp.feature.settings.generated.resources.order_detail_state_canceled_title
import dream_diary_cmp.feature.settings.generated.resources.order_detail_state_running_description
import dream_diary_cmp.feature.settings.generated.resources.order_detail_state_running_negative_action_label
import dream_diary_cmp.feature.settings.generated.resources.order_detail_state_running_title
import dream_diary_cmp.feature.settings.generated.resources.order_detail_state_saved_description
import dream_diary_cmp.feature.settings.generated.resources.order_detail_state_saved_negative_action_label
import dream_diary_cmp.feature.settings.generated.resources.order_detail_state_saved_positive_action_label
import dream_diary_cmp.feature.settings.generated.resources.order_detail_state_saved_title
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.ui.common.icons.sync_saved_locally
import me.bumiller.ddcmp.ui.common.translation.translateName
import me.ddcmp.feature.settings.entries.order_detail.model.OrderState
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

internal fun OrderState.translateDescriptionAndTitle() = Res.string.run {
    when (this@translateDescriptionAndTitle) {
        OrderState.Saved -> order_detail_state_saved_description to order_detail_state_saved_title
        OrderState.Running -> order_detail_state_running_description to order_detail_state_running_title
        OrderState.Canceled -> order_detail_state_canceled_description to order_detail_state_canceled_title
    }
}

internal fun OrderState.icon() =
    when (this) {
        OrderState.Saved -> Icons.Default.Schedule
        OrderState.Running -> sync_saved_locally
        OrderState.Canceled -> Icons.Default.UpdateDisabled
    }

internal fun OrderState.positiveActionLabelAndIcon() = Res.string.run {
    when (this@positiveActionLabelAndIcon) {
        OrderState.Saved -> Icons.Default.PlayArrow to order_detail_state_saved_positive_action_label
        OrderState.Running -> null
        OrderState.Canceled -> null
    }
}

internal fun OrderState.negativeActionLabelAndIcon() = Res.string.run {
    when (this@negativeActionLabelAndIcon) {
        OrderState.Saved -> Icons.Default.Delete to order_detail_state_saved_negative_action_label
        OrderState.Running -> Icons.Outlined.StopCircle  to order_detail_state_running_negative_action_label
        OrderState.Canceled -> null
    }
}

private fun Duration.getDaysPart() = inWholeHours / 24
private fun Duration.getHoursPart() = inWholeHours % 24

@Composable
internal fun PeriodicBackupOrder.translateSchedule() =
    (period.getDaysPart() to period.getHoursPart()).let { (days, hours) ->
        if (hours == 0L) stringResource(Res.string.order_detail_schedule_value_days, days)
        else if (days == 0L) stringResource(
            Res.string.order_detail_schedule_value_hours,
            hours
        )
        else stringResource(Res.string.order_detail_schedule_value, days, hours)
    }

@Composable
internal fun ExportOption.translateFileType() = when (this) {
    ExportOption.Legacy -> stringResource(Res.string.order_detail_filetype_legacy)
    is ExportOption.Normal -> fileType.translateName
}

@Composable
internal fun Int?.translateKeepLast() = when (this) {
    null -> stringResource(Res.string.order_detail_keeplast_unlimited)
    else -> stringResource(Res.string.order_detail_keeplast_limited, this)
}

@Composable
internal fun Int.translateBackupCount() = when (this) {
    0 -> stringResource(Res.string.order_detail_backup_count_none)
    else -> toString()
}