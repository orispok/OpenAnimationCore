package com.osg.openanimation.core.ui.components.loading

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.components.lottie.LottieAnimationView
import com.osg.openanimation.core.ui.generated.resources.Res


@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
) {
    val animationsState = animationResource("loading_animation.json")
    LottieAnimationView(
        animationData = animationsState,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}

@Composable
fun animationResource(
    fileName: String,
): AnimationDataState.LazyLoading{
    return AnimationDataState.LazyLoading(
        fileName,
    ){
        Res.readBytes(fileName).decodeToString()
    }
}