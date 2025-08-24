package com.osg.openanimation.core.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.ui.components.lottie.AnimationCard
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialogView
import com.osg.openanimation.core.ui.dashboard.state.DashboardUiState
import com.osg.openanimation.core.ui.dashboard.state.UploadedAnimationUiData
import com.osg.openanimation.core.ui.util.adaptive.ScreenSizeClass
import com.osg.openanimation.core.ui.util.adaptive.currentScreenWidthClass

@Composable
fun DashboardView(
    modifier: Modifier = Modifier,
    dashboardUiState: DashboardUiState,
    onFileDropped: (String) -> Unit,
    onAnimationClick: (String) -> Unit,

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

            is DashboardUiState.UploadedCollection -> {
                UploadedCollectionView(
                    uploadedAnimations = dashboardUiState.animations,
                    onAnimationClick = onAnimationClick,
                )
            }

            is DashboardUiState.UploadedAnimationDetail -> {

            }
        }
    }
}


@Composable
fun UploadedCollectionView(
    modifier: Modifier = Modifier,
    uploadedAnimations: List<UploadedAnimationUiData>,
    onAnimationClick: (String) -> Unit,
) {
    val columnCount = when(currentScreenWidthClass){
        ScreenSizeClass.COMPACT -> 1
        ScreenSizeClass.MEDIUM -> 2
        ScreenSizeClass.EXPANDED -> 3
    }
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(columnCount),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = uploadedAnimations,
            key = { it.metadata.animationId },
        ) { animationsData ->
            AnimationUploadedCard(
                modifier = Modifier.padding(horizontal = 8.dp).width(300.dp).height(400.dp),
                animationData = animationsData,
                onClick = {
                    onAnimationClick(animationsData.metadata.animationId)
                }
            )
        }
    }
}

@Composable
fun AnimationUploadedCard(
    animationData: UploadedAnimationUiData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    Column(
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimationCard(
            modifier = Modifier
                .weight(1f),
            animationState = animationData.animationState,
            onClick = onClick
        )
        AnimationQuickActions(
            modifier = Modifier,
            animationMetadata = animationData.metadata
        )
    }
}


@Composable
fun AnimationQuickActions(
    modifier: Modifier = Modifier,
    animationMetadata: UploadedAnimationMeta
) {
    ListItem(
        headlineContent = { Text(animationMetadata.name) },
        leadingContent = {},
        trailingContent = {
        },
        modifier = modifier
    )
}