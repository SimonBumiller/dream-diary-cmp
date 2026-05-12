package me.bumiller.ddcmp.ui.common.style

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.graphics.shapes.RoundedPolygon
import me.bumiller.ddcmp.domain.model.EmotionType

/**
 * The color associated with the emotion type
 */
val EmotionType.color: Color
    get() = when (this) {
        EmotionType.Joy -> Color(0xff77dd77)
        EmotionType.Pleasure -> Color(0xff99607d) // Purple
        EmotionType.Sadness -> Color(0xFF407BC1) // Blue
        EmotionType.Anger -> Color(0xffFF6961) // Red
    }

/**
 * The shape associated with the emotion type.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val EmotionType.shape: RoundedPolygon
    get() = when (this) {
        EmotionType.Joy -> MaterialShapes.Flower
        EmotionType.Pleasure -> MaterialShapes.Sunny
        EmotionType.Sadness -> MaterialShapes.Oval
        EmotionType.Anger -> MaterialShapes.Boom
    }