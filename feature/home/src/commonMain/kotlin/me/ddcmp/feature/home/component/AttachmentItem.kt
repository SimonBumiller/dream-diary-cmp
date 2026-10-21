package me.ddcmp.feature.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta
import me.bumiller.ddcmp.ui.common.format.formatDataAmount
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.bumiller.ddcmp.ui.common.translation.mimeTypeIcon
import me.ddcmp.feature.home.entries.dreamadd.DreamAddViewModel

@Composable
internal fun AttachmentItem(
    modifier: Modifier = Modifier,
    meta: DreamAddViewModel.UiAttachment,
    onClick: () -> Unit,
    onRemove: (() -> Unit)? = null,
    onDownload: (() -> Unit)? = null,
    isNew: Boolean = false
) {
    val shape = RoundedCornerShape(12.dp)

    val outlineColor = when (isNew) {
        true -> MaterialTheme.colorScheme.tertiary
        false -> MaterialTheme.colorScheme.outlineVariant
    }

    Row(
        modifier = modifier
            .clip(shape)
            .border(
                width = 2.dp,
                color = outlineColor,
                shape = shape
            )
            .clickable(
                onClick = onClick
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .padding(horizontal = 4.dp),
            imageVector = meta.mimeType.mimeTypeIcon(),
            contentDescription = meta.mimeType
        )

        Column(
            modifier = Modifier
                .weight(1F),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = meta.name,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            DisabledContent {
                Text(
                    text = meta.fileName,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.MiddleEllipsis,
                    maxLines = 1
                )
            }
        }


        Text(
            text = meta.size.formatDataAmount(),
            style = MaterialTheme.typography.bodyMedium
        )

        onRemove?.let {
            IconButton(
                onClick = onRemove,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Remove,
                    contentDescription = null
                )
            }
        }

        onDownload?.let {
            IconButton(
                onClick = onDownload
            ) {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    contentDescription = null
                )
            }
        }
    }
}