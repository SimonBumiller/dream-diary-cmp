package me.ddcmp.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun OverviewFeatureCard(
    modifier: Modifier = Modifier,
    name: String,
    count: Int,
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: Color
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        border = BorderStroke(width = 1.dp, color = containerColor),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = containerColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 16.dp,
                    horizontal = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
            Spacer(
                modifier = Modifier
                    .width(8.dp)
            )
            Text(
                modifier = Modifier
                    .padding(end = 4.dp),
                text = name,
                style = MaterialTheme.typography.titleMedium
                    .copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}