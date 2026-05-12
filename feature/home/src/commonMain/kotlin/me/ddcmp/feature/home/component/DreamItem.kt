package me.ddcmp.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.dream_title
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.EmotionType
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.ui.common.format.formatInstant
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.bumiller.ddcmp.ui.common.style.icon
import me.ddcmp.feature.home.style.HomeStyles
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@Composable
private fun selectedCardColors() = CardDefaults.cardColors()
    .copy(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = contentColorFor(MaterialTheme.colorScheme.secondaryContainer)
    )

@Composable
internal fun DreamItem(
    modifier: Modifier,
    dream: Dream,
    emotions: List<Emotion>,
    persons: List<Person>,
    onClick: () -> Unit,
    selected: Boolean
) {
    Card(
        modifier = modifier,
        colors = if (selected) selectedCardColors() else CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TitleComponent(Modifier, dream)

                Icon(
                    imageVector = dream.confidentiality.icon,
                    contentDescription = null,
                    tint = LocalContentColor.current.copy(alpha = 0.38F)
                )
            }

            if (dream.confidentiality.previewDescription) {
                Text(
                    text = dream.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1F),
                    contentAlignment = Alignment.Center
                ) {
                    val emotionTypes = emotions
                        .distinctBy { EmotionType.from(it) }
                    SimpleEmotionBadge(
                        emotions = emotionTypes
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1F),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = persons.size.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )

                    DisabledContent {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1F),
                    contentAlignment = Alignment.Center
                ) {
                    DisabledContent {
                        Icon(
                            imageVector = if (dream.favorite) Icons.Filled.Favorite
                            else Icons.Outlined.Favorite,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TitleComponent(
    modifier: Modifier,
    dream: Dream
) {
    val showTitle = dream.confidentiality.previewTitle
    val titleExists = dream.title != null

    Column(
        modifier = modifier
    ) {
        if (showTitle && titleExists) {
            Text(
                text = formatInstant(dream.dreamt),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = dream.title!!,
                style = HomeStyles.dreamItemTitleAlternative,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        } else {
            Text(
                text = stringResource(Res.string.dream_title, formatInstant(dream.dreamt)),
                style = HomeStyles.dreamItemTitle
            )
        }
    }
}