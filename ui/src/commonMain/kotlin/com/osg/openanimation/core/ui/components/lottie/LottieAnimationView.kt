package com.osg.openanimation.core.ui.components.lottie
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieAnimatable
import io.github.alexzhirkevich.compottie.rememberLottiePainter


@Composable
fun LottieAnimationView(
    animationData: AnimationDataState,
    iterations: Int = Compottie.IterateForever,
    modifier: Modifier = Modifier,
    clipSpec: ClipSpecProgress = ClipSpecProgress(0f, 1f),
    contentScale: ContentScale = ContentScale.Fit,
){
    val composition by animationData.compositionResult
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        clipSpec = clipSpec.lottieClipSpec
    )
    val painter = rememberLottiePainter(
        composition = composition,
        progress = { progress },
    )
    Box(
        modifier = modifier.fillMaxSize(),
    ){
        AnimatedVisibility(
            enter = fadeIn() + scaleIn(),
            visible = composition != null,
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentDescription = "Lottie animation",
                contentScale = contentScale
            )
        }
    }
}

@Composable
fun lottieClippedAnimation(
    animationData: AnimationDataState,
    animationClips: List<ClipSpecProgress>,
    clipIdx: Int,
    speed: Float = 1.25f,
    onProgressChanged: (Float) -> Unit = {},
): Painter {
    val composition by animationData.compositionResult

    val lottieAnimatable = rememberLottieAnimatable()
    var onInit by remember { mutableStateOf(false) }

    LaunchedEffect(clipIdx) {
        val idx = clipIdx % animationClips.size
        if (!onInit) {
            onInit = true
            lottieAnimatable.snapTo(composition = composition,
                progress = animationClips[idx].max
            )
        } else {
            val spec = animationClips[idx]
//            //AppLogger.d(AppLogger.d("animate from ${spec.min} to ${spec.max}")
            lottieAnimatable.animate(
                composition = composition,
                speed = speed,
                clipSpec = spec.lottieClipSpec
            )
        }
    }
    LaunchedEffect(lottieAnimatable.progress) {
        onProgressChanged(
            lottieAnimatable.progress
        )
    }
    return rememberLottiePainter(
        composition = composition,
        progress = { lottieAnimatable.value },
    )

}