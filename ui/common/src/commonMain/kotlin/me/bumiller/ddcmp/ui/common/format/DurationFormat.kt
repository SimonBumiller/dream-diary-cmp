package me.bumiller.ddcmp.ui.common.format

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.duration_days
import dream_diary_cmp.ui.common.generated.resources.duration_hours
import dream_diary_cmp.ui.common.generated.resources.duration_millis
import dream_diary_cmp.ui.common.generated.resources.duration_minutes
import dream_diary_cmp.ui.common.generated.resources.duration_nanos
import dream_diary_cmp.ui.common.generated.resources.duration_seconds
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

/**
 * Formats a time-duration with a fitting unit.
 */
@Composable
fun Duration.formatDurationLength(): String =
    if (inWholeDays > 1) stringResource(Res.string.duration_days, inWholeDays)
    else if (inWholeHours > 1) stringResource(Res.string.duration_hours, inWholeHours)
    else if (inWholeMinutes > 1) stringResource(Res.string.duration_minutes, inWholeMinutes)
    else if (inWholeSeconds > 1) stringResource(Res.string.duration_seconds, inWholeSeconds)
    else if (inWholeMilliseconds > 1) stringResource(
        Res.string.duration_millis,
        inWholeMilliseconds
    )
    else stringResource(Res.string.duration_nanos, inWholeNanoseconds)