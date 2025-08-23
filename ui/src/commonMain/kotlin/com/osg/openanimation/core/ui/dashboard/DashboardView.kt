package com.osg.openanimation.core.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialogView
import com.osg.openanimation.core.ui.dashboard.state.DashboardUiState
import com.osg.openanimation.core.ui.dashboard.state.UploadedAnimationUiData

@Composable
fun DashboardView(
    modifier: Modifier = Modifier,
    dashboardUiState: DashboardUiState,
    onFileDropped: (String) -> Unit,
    onAnimationClick: (UploadedAnimationUiData) -> Unit,

) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        when (dashboardUiState) {
            is DashboardUiState.SignedOut -> {
                SignInReasoningDialogView(
                    modifier = Modifier.align(Alignment.TopCenter),
                )

            }

            is DashboardUiState.Empty -> {
                DragAndDropLottieJsonView(
                    state = DragAndDropState.Initial,
                    onFileDropped = onFileDropped,
                )
            }

            is DashboardUiState.UploadedAnimations -> {
                UploadedAnimationsView(
                    animations = dashboardUiState.animations,
                    onAnimationClick = onAnimationClick,
                )
            }

            is DashboardUiState.UploadedAnimationDetail -> {

            }
        }
    }
}


@Composable
fun UploadedAnimationsView(
    modifier: Modifier = Modifier,
    animations: List<UploadedAnimationUiData>,
    onAnimationClick: (UploadedAnimationUiData) -> Unit,
) {

}