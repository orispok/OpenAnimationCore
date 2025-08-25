package com.osg.openanimation.core.ui.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.osg.openanimation.core.ui.components.loading.LoadingAnimation
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialogView
import com.osg.openanimation.core.ui.graph.Destination
import com.osg.openanimation.core.ui.home.domain.ExploreScreenStates


@Composable
fun HomePanel(
    modifier: Modifier = Modifier,
    uiData: ExploreScreenStates,
    onNavigation: (Destination) -> Unit,
) {
    when (uiData) {
        is ExploreScreenStates.Success -> {
            AnimationGrid(
                modifier = modifier,
                screenData = uiData,
                onDestination = { dst ->
                    onNavigation(dst)
                }
            )
        }

        ExploreScreenStates.RequiredLogin -> {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                SignInReasoningDialogView(
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }

        ExploreScreenStates.Loading -> {
            LoadingAnimation(
                modifier = modifier.fillMaxSize()
            )
        }
    }
}