package com.osg.openanimation.core.ui.util.icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.Upload: ImageVector
    get() {
        if (_upload != null) {
            return _upload!!
        }
        _upload = materialIcon(name = "Filled.Upload") {
            materialPath {
                moveTo(5.0f, 20.0f)
                horizontalLineToRelative(14.0f)
                verticalLineToRelative(-2.0f)
                horizontalLineTo(5.0f)
                verticalLineTo(20.0f)
                close()
                moveTo(5.0f, 10.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(6.0f)
                horizontalLineToRelative(6.0f)
                verticalLineToRelative(-6.0f)
                horizontalLineToRelative(4.0f)
                lineToRelative(-7.0f, -7.0f)
                lineTo(5.0f, 10.0f)
                close()
            }
        }
        return _upload!!
    }

private var _upload: ImageVector? = null
