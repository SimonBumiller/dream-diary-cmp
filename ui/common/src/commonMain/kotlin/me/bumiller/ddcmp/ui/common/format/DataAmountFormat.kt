package me.bumiller.ddcmp.ui.common.format

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.data_amount_byte
import dream_diary_cmp.ui.common.generated.resources.data_amount_gigabyte
import dream_diary_cmp.ui.common.generated.resources.data_amount_kilobyte
import dream_diary_cmp.ui.common.generated.resources.data_amount_megabyte
import org.jetbrains.compose.resources.stringResource

/**
 * Formats a byte count to normalized data count and unit
 */
@Composable
fun Long.formatDataAmount(): String =
    if(this < 5_000) stringResource(Res.string.data_amount_byte, this)
    else if(this < 5_000_000) stringResource(Res.string.data_amount_kilobyte, this / 1000)
    else if(this < 5_000_000_000) stringResource(Res.string.data_amount_megabyte, this / 1000_000)
    else stringResource(Res.string.data_amount_gigabyte, this / 1000_000_000)