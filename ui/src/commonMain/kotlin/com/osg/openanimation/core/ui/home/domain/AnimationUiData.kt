package com.osg.openanimation.core.ui.home.domain

import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.ui.color.model.ColorsEditPalette
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState

@Immutable
data class AnimationUiData(
    val animationState: AnimationDataState,
    val metadata: AnimationMetadata,
)

@Immutable
data class ColorPaletteWithMetadata(
    val editableAnimation: ColorsEditPalette,
    val metadata: AnimationMetadata,
)