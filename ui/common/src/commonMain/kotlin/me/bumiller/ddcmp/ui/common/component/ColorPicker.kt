package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

private val colors = listOf(
    listOf(0xFFB83A42, 0xFFD05252, 0xFFA52D35, 0xFFC45B64),
    listOf(0xFFB86A28, 0xFFCC7A32, 0xFFA85B20, 0xFFD08B45),
    listOf(0xFF8A8500, 0xFF9B9220, 0xFF6F8528, 0xFF819B35),
    listOf(0xFF23875A, 0xFF32976C, 0xFF16847E, 0xFF369A91),
    listOf(0xFF3478B8, 0xFF4969C4, 0xFF2864A3, 0xFF557FC7),
    listOf(0xFF8853B5, 0xFFA14F9E, 0xFFB54F83, 0xFF7957A8)
)

const val ColorPickerInitialColor = 0xFFB83A42

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    selectedColor: Long?,
    onColorChange: (Long) -> Unit
) {
    var popupVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(Color(selectedColor ?: colors.flatten().first()))
            .clickable { popupVisible = !popupVisible }
    ) {
        if(popupVisible) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { popupVisible = false }
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        for (column in colors) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                for (color in column) {
                                    Box(
                                        modifier = modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(Color(color))
                                            .let {
                                                if(color == selectedColor) it.border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                                else it
                                            }
                                            .clickable {
                                                onColorChange(color)
                                                popupVisible = false
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}