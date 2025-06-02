package com.osg.openanimation.core.ui.components.signin.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val appleIcon: ImageVector
    get() = ImageVector.Builder(
        name = "AppleIcon", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
        viewportWidth = 48.0f, viewportHeight = 48.0f
    ).apply {
        path(fill = SolidColor(Color(0xFF0B0B0A)), stroke = SolidColor(Color(0x00000000)),
            strokeLineWidth = 1.0f, strokeLineCap = StrokeCap.Companion.Butt, strokeLineJoin = StrokeJoin.Companion.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.Companion.EvenOdd
        ) {
            moveTo(30.675f, 7.792f)
            curveTo(32.24f, 5.772f, 33.427f, 2.915f, 32.998f, 0.0f)
            curveTo(30.439f, 0.178f, 27.448f, 1.815f, 25.703f, 3.948f)
            curveTo(24.113f, 5.882f, 22.805f, 8.758f, 23.316f, 11.549f)
            curveTo(26.113f, 11.637f, 29.0f, 9.96f, 30.675f, 7.792f)
            lineTo(30.675f, 7.792f)
            close()
            moveTo(44.5f, 35.217f)
            curveTo(43.381f, 37.712f, 42.842f, 38.827f, 41.4f, 41.037f)
            curveTo(39.388f, 44.122f, 36.552f, 47.963f, 33.034f, 47.991f)
            curveTo(29.912f, 48.025f, 29.106f, 45.945f, 24.867f, 45.97f)
            curveTo(20.628f, 45.993f, 19.745f, 48.031f, 16.617f, 48.0f)
            curveTo(13.101f, 47.969f, 10.414f, 44.503f, 8.402f, 41.418f)
            curveTo(2.775f, 32.798f, 2.183f, 22.68f, 5.653f, 17.298f)
            curveTo(8.122f, 13.477f, 12.016f, 11.241f, 15.674f, 11.241f)
            curveTo(19.397f, 11.241f, 21.739f, 13.296f, 24.823f, 13.296f)
            curveTo(27.813f, 13.296f, 29.634f, 11.236f, 33.94f, 11.236f)
            curveTo(37.2f, 11.236f, 40.654f, 13.022f, 43.111f, 16.104f)
            curveTo(35.054f, 20.547f, 36.359f, 32.121f, 44.5f, 35.217f)
            lineTo(44.5f, 35.217f)
            close()
        }
    }.build()