package com.osg.openanimation.core.ui.util.icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.RoomService: ImageVector
    get() {
        if (_roomService != null) {
            return _roomService!!
        }
        _roomService = materialIcon(name = "Filled.RoomService") {
            materialPath {
                moveTo(2.0f, 17.0f)
                horizontalLineToRelative(20.0f)
                verticalLineToRelative(2.0f)
                lineTo(2.0f, 19.0f)
                close()
                moveTo(13.84f, 7.79f)
                curveToRelative(0.1f, -0.24f, 0.16f, -0.51f, 0.16f, -0.79f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                reflectiveCurveToRelative(-2.0f, 0.9f, -2.0f, 2.0f)
                curveToRelative(0.0f, 0.28f, 0.06f, 0.55f, 0.16f, 0.79f)
                curveTo(6.25f, 8.6f, 3.27f, 11.93f, 3.0f, 16.0f)
                horizontalLineToRelative(18.0f)
                curveToRelative(-0.27f, -4.07f, -3.25f, -7.4f, -7.16f, -8.21f)
                close()
            }
        }
        return _roomService!!
    }

private var _roomService: ImageVector? = null
