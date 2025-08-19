package com.osg.openanimation.core.ui.components.lottie

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AnimationDataState {
    data class LazyLoading(
        val hash: String,
        val lazyLoader: suspend () -> String,
    ) : AnimationDataState

    data object Processing : AnimationDataState
}