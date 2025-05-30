package com.osg.multibase.signin.domain.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val googleIcon: ImageVector
    get()  = ImageVector.Builder(
        name = "GoogleIcon", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 48.0f, viewportHeight = 48.0f
    ).apply {
        path(fill = SolidColor(Color(0xFFFBBC05)), stroke = SolidColor(Color(0x00000000)),
            strokeLineWidth = 1.0f, strokeLineCap = StrokeCap.Companion.Butt, strokeLineJoin = StrokeJoin.Companion.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.Companion.EvenOdd
        ) {
            moveTo(10.327f, 24.0f)
            curveTo(10.327f, 22.476f, 10.58f, 21.014f, 11.032f, 19.644f)
            lineTo(3.123f, 13.604f)
            curveTo(1.582f, 16.734f, 0.714f, 20.26f, 0.714f, 24.0f)
            curveTo(0.714f, 27.737f, 1.581f, 31.261f, 3.12f, 34.388f)
            lineTo(11.025f, 28.337f)
            curveTo(10.577f, 26.973f, 10.327f, 25.517f, 10.327f, 24.0f)
        }
        path(fill = SolidColor(Color(0xFFEB4335)), stroke = SolidColor(Color(0x00000000)),
            strokeLineWidth = 1.0f, strokeLineCap = StrokeCap.Companion.Butt, strokeLineJoin = StrokeJoin.Companion.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.Companion.EvenOdd
        ) {
            moveTo(24.214f, 10.133f)
            curveTo(27.525f, 10.133f, 30.516f, 11.307f, 32.866f, 13.227f)
            lineTo(39.702f, 6.4f)
            curveTo(35.536f, 2.773f, 30.195f, 0.533f, 24.214f, 0.533f)
            curveTo(14.927f, 0.533f, 6.945f, 5.844f, 3.123f, 13.604f)
            lineTo(11.032f, 19.644f)
            curveTo(12.855f, 14.112f, 18.049f, 10.133f, 24.214f, 10.133f)
        }
        path(fill = SolidColor(Color(0xFF34A853)), stroke = SolidColor(Color(0x00000000)),
            strokeLineWidth = 1.0f, strokeLineCap = StrokeCap.Companion.Butt, strokeLineJoin = StrokeJoin.Companion.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.Companion.EvenOdd
        ) {
            moveTo(24.214f, 37.867f)
            curveTo(18.049f, 37.867f, 12.855f, 33.888f, 11.032f, 28.356f)
            lineTo(3.123f, 34.395f)
            curveTo(6.945f, 42.156f, 14.927f, 47.467f, 24.214f, 47.467f)
            curveTo(29.945f, 47.467f, 35.418f, 45.431f, 39.525f, 41.618f)
            lineTo(32.018f, 35.814f)
            curveTo(29.9f, 37.149f, 27.232f, 37.867f, 24.214f, 37.867f)
        }
        path(fill = SolidColor(Color(0xFF4285F4)), stroke = SolidColor(Color(0x00000000)),
            strokeLineWidth = 1.0f, strokeLineCap = StrokeCap.Companion.Butt, strokeLineJoin = StrokeJoin.Companion.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.Companion.EvenOdd
        ) {
            moveTo(46.645f, 24.0f)
            curveTo(46.645f, 22.613f, 46.432f, 21.12f, 46.111f, 19.733f)
            lineTo(24.214f, 19.733f)
            lineTo(24.214f, 28.8f)
            lineTo(36.818f, 28.8f)
            curveTo(36.188f, 31.891f, 34.472f, 34.268f, 32.018f, 35.814f)
            lineTo(39.525f, 41.618f)
            curveTo(43.839f, 37.614f, 46.645f, 31.649f, 46.645f, 24.0f)
        }
    }.build()