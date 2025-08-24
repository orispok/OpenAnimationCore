package com.osg.openanimation.core.ui.dashboard.state

import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState

@Immutable
data class UploadedAnimationUiData(
    val animationState: AnimationDataState,
    val metadata: UploadedAnimationMeta,
)

sealed interface DashboardUiState {
    data object  Loading : DashboardUiState
    data object SignedOut : DashboardUiState
    data object Empty: DashboardUiState
    data class UploadedCollection(
        val animations: List<UploadedAnimationUiData>,
    ) : DashboardUiState
}