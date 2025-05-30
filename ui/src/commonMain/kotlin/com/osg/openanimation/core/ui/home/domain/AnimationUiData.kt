package com.osg.openanimation.core.ui.home.domain

import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.data.animation.AnimationMetadata

@Immutable
data class AnimationUiData(
    val animationState: AnimationDataState,
    val metadata: AnimationMetadata,
)