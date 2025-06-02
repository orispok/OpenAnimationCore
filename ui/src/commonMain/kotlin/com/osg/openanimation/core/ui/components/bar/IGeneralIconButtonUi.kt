package com.osg.openanimation.core.ui.components.bar
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

interface IGeneralIconButtonUi {
    val imageVector: ImageVector
    val stringResource: StringResource
}

data class GeneralIconButtonItem(
    override val imageVector: ImageVector,
    override val stringResource: StringResource,
    val onClick: () -> Unit
): IGeneralIconButtonUi