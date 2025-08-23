package com.osg.openanimation.core.ui.dashboard.state

import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.data.upload.UploadedAnimation
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState

@Immutable
data class UploadedAnimationUiData(
    val animationState: AnimationDataState,
    val metadata: UploadedAnimation,
)

sealed interface DashboardUiState {
    data object SignedOut : DashboardUiState
    data object Empty: DashboardUiState
    data class UploadedAnimations(
        val animations: List<UploadedAnimationUiData>,
    ) : DashboardUiState

    data class UploadedAnimationDetail(
        val animation: UploadedAnimationUiData,
    ) : DashboardUiState
}