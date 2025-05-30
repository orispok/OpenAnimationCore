package com.osg.appUiLayer.util.icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.Flag: ImageVector
    get() {
        if (_flag != null) {
            return _flag!!
        }
        _flag = materialIcon(name = "Filled.Flag") {
            materialPath {
                moveTo(14.4f, 6.0f)
                lineTo(14.0f, 4.0f)
                horizontalLineTo(5.0f)
                verticalLineToRelative(17.0f)
                horizontalLineToRelative(2.0f)
                verticalLineToRelative(-7.0f)
                horizontalLineToRelative(5.6f)
                lineToRelative(0.4f, 2.0f)
                horizontalLineToRelative(7.0f)
                verticalLineTo(6.0f)
                close()
            }
        }
        return _flag!!
    }

private var _flag: ImageVector? = null