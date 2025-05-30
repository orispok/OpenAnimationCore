package com.osg.appUiLayer.home.domain

import androidx.compose.runtime.Immutable
import com.osg.appUiLayer.components.lottie.AnimationDataState
import org.osg.previewopenanimation.core.data.animation.AnimationMetadata

@Immutable
data class AnimationUiData(
    val animationState: AnimationDataState,
    val metadata: AnimationMetadata,
)