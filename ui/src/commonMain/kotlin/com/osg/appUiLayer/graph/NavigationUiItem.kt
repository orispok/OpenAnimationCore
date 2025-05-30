package com.osg.appUiLayer.graph

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

@Immutable
data class NavigationUiItem(
    val iconOutline: ImageVector,
    val iconFilled: ImageVector,
    val stringResource: StringResource,
){
    fun resolveOnSelected(isSelected: Boolean) = if (isSelected) iconFilled else iconOutline
}