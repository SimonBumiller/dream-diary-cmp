package me.bumiller.ddcmp.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val database_off: ImageVector
  get() {
    if (_database_off != null) {
      return _database_off!!
    }
    _database_off =
      ImageVector.Builder(
          name = "database_off",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(11.15f, 12.85f)
            close()
            moveTo(13.8f, 10.48f)
            close()
            moveToRelative(-2.65f, 2.38f)
            close()
            moveTo(13.8f, 10.48f)
            close()
            moveToRelative(-2.65f, 2.38f)
            close()
            moveTo(13.8f, 10.48f)
            close()
            moveTo(19.78f, 22.6f)
            lineTo(1.4f, 4.22f)
            lineTo(2.8f, 2.8f)
            lineTo(21.2f, 21.2f)
            lineToRelative(-1.43f, 1.4f)
            close()
            moveTo(12f, 21f)
            quadTo(8.23f, 21f, 5.61f, 19.84f)
            quadTo(3f, 18.68f, 3f, 17f)
            verticalLineTo(7f)
            quadTo(3f, 6.35f, 3.44f, 5.76f)
            reflectiveQuadTo(4.68f, 4.67f)
            lineToRelative(6.3f, 6.3f)
            quadTo(9.18f, 10.9f, 7.65f, 10.52f)
            reflectiveQuadTo(5f, 9.52f)
            verticalLineToRelative(3f)
            quadToRelative(1.28f, 0.73f, 3.08f, 1.1f)
            reflectiveQuadTo(12f, 14f)
            quadToRelative(0.5f, 0f, 0.98f, -0.01f)
            quadToRelative(0.47f, -0.01f, 0.95f, -0.06f)
            lineToRelative(1.75f, 1.75f)
            quadToRelative(-0.85f, 0.18f, -1.78f, 0.25f)
            reflectiveQuadTo(12f, 16f)
            quadTo(9.88f, 16f, 8.08f, 15.63f)
            reflectiveQuadTo(5f, 14.53f)
            verticalLineTo(17f)
            quadToRelative(0.23f, 0.73f, 2.44f, 1.36f)
            reflectiveQuadTo(12f, 19f)
            quadToRelative(1.6f, 0f, 3.21f, -0.32f)
            reflectiveQuadToRelative(2.66f, -0.8f)
            lineToRelative(1.45f, 1.45f)
            quadTo(18.1f, 20.1f, 16.19f, 20.55f)
            quadTo(14.28f, 21f, 12f, 21f)
            close()
            moveToRelative(8.75f, -3.07f)
            lineTo(19f, 16.18f)
            verticalLineTo(14.53f)
            quadToRelative(-0.27f, 0.15f, -0.55f, 0.27f)
            quadToRelative(-0.27f, 0.13f, -0.57f, 0.25f)
            lineTo(16.35f, 13.52f)
            quadToRelative(0.75f, -0.2f, 1.41f, -0.44f)
            quadTo(18.43f, 12.85f, 19f, 12.52f)
            verticalLineToRelative(-3f)
            quadToRelative(-1.02f, 0.58f, -2.35f, 0.93f)
            reflectiveQuadToRelative(-2.9f, 0.47f)
            lineToRelative(-1.9f, -1.9f)
            quadToRelative(1.1f, 0f, 2.3f, -0.17f)
            quadToRelative(1.2f, -0.18f, 2.24f, -0.46f)
            quadTo(17.43f, 8.1f, 18.14f, 7.74f)
            reflectiveQuadTo(19f, 7.02f)
            quadTo(18.73f, 6.3f, 16.49f, 5.65f)
            reflectiveQuadTo(12f, 5f)
            quadTo(11.08f, 5f, 10.11f, 5.13f)
            reflectiveQuadTo(8.28f, 5.45f)
            lineTo(6.63f, 3.8f)
            quadTo(7.75f, 3.42f, 9.13f, 3.21f)
            reflectiveQuadTo(12f, 3f)
            quadToRelative(3.73f, 0f, 6.36f, 1.17f)
            reflectiveQuadTo(21f, 7f)
            verticalLineTo(17f)
            quadToRelative(0f, 0.25f, -0.06f, 0.48f)
            reflectiveQuadToRelative(-0.19f, 0.45f)
            close()
          }
        }
        .build()
    return _database_off!!
  }

private var _database_off: ImageVector? = null