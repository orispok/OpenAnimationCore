package com.osg.appUiLayer.util.icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.AutoMirrored.Filled.MenuOpen: ImageVector
    get() {
        if (_menuOpen != null) {
            return _menuOpen!!
        }
        _menuOpen = materialIcon(name = "AutoMirrored.Filled.MenuOpen", autoMirror = true) {
            materialPath {
                moveTo(3.0f, 18.0f)
                horizontalLineToRelative(13.0f)
                verticalLineToRelative(-2.0f)
                lineTo(3.0f, 16.0f)
                verticalLineToRelative(2.0f)
                close()
                moveTo(3.0f, 13.0f)
                horizontalLineToRelative(10.0f)
                verticalLineToRelative(-2.0f)
                lineTo(3.0f, 11.0f)
                verticalLineToRelative(2.0f)
                close()
                moveTo(3.0f, 6.0f)
                verticalLineToRelative(2.0f)
                horizontalLineToRelative(13.0f)
                lineTo(16.0f, 6.0f)
                lineTo(3.0f, 6.0f)
                close()
                moveTo(21.0f, 15.59f)
                lineTo(17.42f, 12.0f)
                lineTo(21.0f, 8.41f)
                lineTo(19.59f, 7.0f)
                lineToRelative(-5.0f, 5.0f)
                lineToRelative(5.0f, 5.0f)
                lineTo(21.0f, 15.59f)
                close()
            }
        }
        return _menuOpen!!
    }

private var _menuOpen: ImageVector? = null
