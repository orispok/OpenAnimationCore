package com.osg.openanimation.core.ui.util.icons


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Outlined.Explore: ImageVector
    get() {
        if (_explore != null) {
            return _explore!!
        }
        _explore = materialIcon(name = "Outlined.Explore") {
            materialPath {
                moveTo(12.0f, 2.0f)
                curveTo(6.48f, 2.0f, 2.0f, 6.48f, 2.0f, 12.0f)
                reflectiveCurveToRelative(4.48f, 10.0f, 10.0f, 10.0f)
                reflectiveCurveToRelative(10.0f, -4.48f, 10.0f, -10.0f)
                reflectiveCurveTo(17.52f, 2.0f, 12.0f, 2.0f)
                close()
                moveTo(12.0f, 20.0f)
                curveToRelative(-4.41f, 0.0f, -8.0f, -3.59f, -8.0f, -8.0f)
                reflectiveCurveToRelative(3.59f, -8.0f, 8.0f, -8.0f)
                reflectiveCurveToRelative(8.0f, 3.59f, 8.0f, 8.0f)
                reflectiveCurveToRelative(-3.59f, 8.0f, -8.0f, 8.0f)
                close()
                moveTo(6.5f, 17.5f)
                lineToRelative(7.51f, -3.49f)
                lineTo(17.5f, 6.5f)
                lineTo(9.99f, 9.99f)
                lineTo(6.5f, 17.5f)
                close()
                moveTo(12.0f, 10.9f)
                curveToRelative(0.61f, 0.0f, 1.1f, 0.49f, 1.1f, 1.1f)
                reflectiveCurveToRelative(-0.49f, 1.1f, -1.1f, 1.1f)
                reflectiveCurveToRelative(-1.1f, -0.49f, -1.1f, -1.1f)
                reflectiveCurveToRelative(0.49f, -1.1f, 1.1f, -1.1f)
                close()
            }
        }
        return _explore!!
    }

private var _explore: ImageVector? = null
