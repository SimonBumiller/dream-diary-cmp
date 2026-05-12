package me.bumiller.ddcmp.ui.common.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * Bundles a text style and a callback.
 */
data class TextStyleWithCallback(

    /**
     * The text style.
     */
    val style: TextStyle,

    /**
     * The callback.
     */
    val callback: (() -> Unit)? = null

)

private const val TagPrefix = "MultiStyleText-Tag-"

/**
 * Utility component to color a text in multiple colors.
 *
 * @param modifier The modifier
 * @param style The text style
 * @param pairs The pairs, assigning each text a specific style and an optional callback.
 */
@Composable
fun MultiStyleText(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    vararg pairs: Pair<String, TextStyleWithCallback>
) {
    Text(
        modifier = modifier,
        style = style,
        text = buildAnnotatedString {
            pairs.forEachIndexed { index, (text, style) ->
                if (style.callback != null) {
                    pushLink(
                        LinkAnnotation.Clickable(
                            tag = "$TagPrefix$index",
                            linkInteractionListener = { style.callback.invoke() }
                        )
                    )
                }

                withStyle(style.style.toSpanStyle()) {
                    println(style.style)
                    append(text)
                }

                if (style.callback != null) {
                    pop()
                }
            }
        }
    )
}