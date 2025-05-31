package com.osg.openanimation.core.ui.components.loading

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.components.lottie.LottieAnimationView

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
) {
    val animationsState = AnimationDataState.fromResource(
        "files/loading_animation.json"
    )
    LottieAnimationView(
        animationData = animationsState,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}