package com.osg.openanimation.core.ui.dashboard.state

import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class UploadedAnimationUiData(
    val animationState: AnimationDataState,
    val metadata: UploadedAnimationMeta,
)

sealed interface DashboardUiState {
    data object  Loading : DashboardUiState
    data object SignedOut : DashboardUiState
    data object Empty: DashboardUiState
    @Immutable
    data class UploadedCollection(
        val animations: ImmutableList<UploadedAnimationUiData>,
    ) : DashboardUiState
}