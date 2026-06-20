package me.bumiller.ddcmp.ui.common.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun TopBarWithProgressIndicator(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    topBar: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .padding(bottom = 4.dp)
    ) {
        topBar()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if(loading) 1F else 0F)
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}