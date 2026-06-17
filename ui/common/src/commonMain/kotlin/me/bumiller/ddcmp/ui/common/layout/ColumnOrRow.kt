package me.bumiller.ddcmp.ui.common.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.bumiller.ddcmp.ui.common.LocalWindowSizeClass

@Composable
fun ColumnOrRow(
    modifier: Modifier = Modifier,
    doColumn: @Composable (sizeClass: WindowSizeClass) -> Boolean,
    first: @Composable (doColumn: Boolean) -> Unit,
    firstWeight: Float = 0.5F,
    second: @Composable (doColumn: Boolean) -> Unit,
    secondWeight: Float = 0.5F,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    val sizeClass = LocalWindowSizeClass.current

    if (doColumn(sizeClass)) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            Box {
                first(true)
            }
            Box{
                second(true)
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize(),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            Box(
                modifier = Modifier
                    .weight(firstWeight)
            ) {
                first(true)
            }
            Box(
                modifier = Modifier
                    .weight(1 - firstWeight)
            ) {
                second(true)
            }
        }
    }
}