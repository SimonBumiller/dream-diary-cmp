package me.bumiller.ddcmp.ui.common.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
 * Builds an annotated string from markdown-like syntax.
 */
fun styledText(input: String): AnnotatedString {
    return buildAnnotatedString {
        var i = 0

        var bold = false
        var italic = false

        while (i < input.length) {

            // Bold: **
            if (i + 1 < input.length && input[i] == '*' && input[i + 1] == '*') {
                bold = !bold
                i += 2
                continue
            }

            // Italic: _
            if (input[i] == '_') {
                italic = !italic
                i++
                continue
            }

            val start = i

            // collect normal text until next marker
            while (i < input.length) {
                if (
                    (i + 1 < input.length && input[i] == '*' && input[i + 1] == '*') ||
                    input[i] == '_'
                ) break
                i++
            }

            val chunk = input.substring(start, i)

            if (chunk.isNotEmpty()) {
                withStyle(
                    SpanStyle(
                        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal
                    )
                ) {
                    append(chunk)
                }
            }
        }
    }
}