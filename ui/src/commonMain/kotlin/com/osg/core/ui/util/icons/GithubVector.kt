package com.osg.core.ui.util.icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val githubVector: ImageVector
    get() {
        return Builder(name = "Githubvector", defaultWidth = 22.0.dp, defaultHeight =
            22.0.dp, viewportWidth = 20.0f, viewportHeight = 20.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = EvenOdd) {
                moveTo(10.0f, 0.0f)
                curveTo(15.523f, 0.0f, 20.0f, 4.59f, 20.0f, 10.253f)
                curveTo(20.0f, 14.782f, 17.138f, 18.624f, 13.167f, 19.981f)
                curveTo(12.66f, 20.082f, 12.48f, 19.762f, 12.48f, 19.489f)
                curveTo(12.48f, 19.151f, 12.492f, 18.047f, 12.492f, 16.675f)
                curveTo(12.492f, 15.719f, 12.172f, 15.095f, 11.813f, 14.777f)
                curveTo(14.04f, 14.523f, 16.38f, 13.656f, 16.38f, 9.718f)
                curveTo(16.38f, 8.598f, 15.992f, 7.684f, 15.35f, 6.966f)
                curveTo(15.454f, 6.707f, 15.797f, 5.664f, 15.252f, 4.252f)
                curveTo(15.252f, 4.252f, 14.414f, 3.977f, 12.505f, 5.303f)
                curveTo(11.706f, 5.076f, 10.85f, 4.962f, 10.0f, 4.958f)
                curveTo(9.15f, 4.962f, 8.295f, 5.076f, 7.497f, 5.303f)
                curveTo(5.586f, 3.977f, 4.746f, 4.252f, 4.746f, 4.252f)
                curveTo(4.203f, 5.664f, 4.546f, 6.707f, 4.649f, 6.966f)
                curveTo(4.01f, 7.684f, 3.619f, 8.598f, 3.619f, 9.718f)
                curveTo(3.619f, 13.646f, 5.954f, 14.526f, 8.175f, 14.785f)
                curveTo(7.889f, 15.041f, 7.63f, 15.493f, 7.54f, 16.156f)
                curveTo(6.97f, 16.418f, 5.522f, 16.871f, 4.63f, 15.304f)
                curveTo(4.63f, 15.304f, 4.101f, 14.319f, 3.097f, 14.247f)
                curveTo(3.097f, 14.247f, 2.122f, 14.234f, 3.029f, 14.87f)
                curveTo(3.029f, 14.87f, 3.684f, 15.185f, 4.139f, 16.37f)
                curveTo(4.139f, 16.37f, 4.726f, 18.2f, 7.508f, 17.58f)
                curveTo(7.513f, 18.437f, 7.522f, 19.245f, 7.522f, 19.489f)
                curveTo(7.522f, 19.76f, 7.338f, 20.077f, 6.839f, 19.982f)
                curveTo(2.865f, 18.627f, 0.0f, 14.783f, 0.0f, 10.253f)
                curveTo(0.0f, 4.59f, 4.478f, 0.0f, 10.0f, 0.0f)
            }
        }.build()
    }
