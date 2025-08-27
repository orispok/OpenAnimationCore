package com.osg.openanimation.core.ui.util.icons.social
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

public val SocialIconPack.LinkedinMonoVec: ImageVector
    get() {
        if (_linkedinMonoVec != null) {
            return _linkedinMonoVec!!
        }
        _linkedinMonoVec = Builder(name = "LinkedinMonoVec", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF0F0F0F)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(6.5f, 8.0f)
                curveTo(7.328f, 8.0f, 8.0f, 7.328f, 8.0f, 6.5f)
                curveTo(8.0f, 5.672f, 7.328f, 5.0f, 6.5f, 5.0f)
                curveTo(5.672f, 5.0f, 5.0f, 5.672f, 5.0f, 6.5f)
                curveTo(5.0f, 7.328f, 5.672f, 8.0f, 6.5f, 8.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF0F0F0F)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(5.0f, 10.0f)
                curveTo(5.0f, 9.448f, 5.448f, 9.0f, 6.0f, 9.0f)
                horizontalLineTo(7.0f)
                curveTo(7.552f, 9.0f, 8.0f, 9.448f, 8.0f, 10.0f)
                verticalLineTo(18.0f)
                curveTo(8.0f, 18.552f, 7.552f, 19.0f, 7.0f, 19.0f)
                horizontalLineTo(6.0f)
                curveTo(5.448f, 19.0f, 5.0f, 18.552f, 5.0f, 18.0f)
                verticalLineTo(10.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF0F0F0F)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(11.0f, 19.0f)
                horizontalLineTo(12.0f)
                curveTo(12.552f, 19.0f, 13.0f, 18.552f, 13.0f, 18.0f)
                verticalLineTo(13.5f)
                curveTo(13.0f, 12.0f, 16.0f, 11.0f, 16.0f, 13.0f)
                verticalLineTo(18.0f)
                curveTo(16.0f, 18.553f, 16.448f, 19.0f, 17.0f, 19.0f)
                horizontalLineTo(18.0f)
                curveTo(18.552f, 19.0f, 19.0f, 18.552f, 19.0f, 18.0f)
                verticalLineTo(12.0f)
                curveTo(19.0f, 10.0f, 17.5f, 9.0f, 15.5f, 9.0f)
                curveTo(13.5f, 9.0f, 13.0f, 10.5f, 13.0f, 10.5f)
                verticalLineTo(10.0f)
                curveTo(13.0f, 9.448f, 12.552f, 9.0f, 12.0f, 9.0f)
                horizontalLineTo(11.0f)
                curveTo(10.448f, 9.0f, 10.0f, 9.448f, 10.0f, 10.0f)
                verticalLineTo(18.0f)
                curveTo(10.0f, 18.552f, 10.448f, 19.0f, 11.0f, 19.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF0F0F0F)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd) {
                moveTo(20.0f, 1.0f)
                curveTo(21.657f, 1.0f, 23.0f, 2.343f, 23.0f, 4.0f)
                verticalLineTo(20.0f)
                curveTo(23.0f, 21.657f, 21.657f, 23.0f, 20.0f, 23.0f)
                horizontalLineTo(4.0f)
                curveTo(2.343f, 23.0f, 1.0f, 21.657f, 1.0f, 20.0f)
                verticalLineTo(4.0f)
                curveTo(1.0f, 2.343f, 2.343f, 1.0f, 4.0f, 1.0f)
                horizontalLineTo(20.0f)
                close()
                moveTo(20.0f, 3.0f)
                curveTo(20.552f, 3.0f, 21.0f, 3.448f, 21.0f, 4.0f)
                verticalLineTo(20.0f)
                curveTo(21.0f, 20.552f, 20.552f, 21.0f, 20.0f, 21.0f)
                horizontalLineTo(4.0f)
                curveTo(3.448f, 21.0f, 3.0f, 20.552f, 3.0f, 20.0f)
                verticalLineTo(4.0f)
                curveTo(3.0f, 3.448f, 3.448f, 3.0f, 4.0f, 3.0f)
                horizontalLineTo(20.0f)
                close()
            }
        }
        .build()
        return _linkedinMonoVec!!
    }

private var _linkedinMonoVec: ImageVector? = null
