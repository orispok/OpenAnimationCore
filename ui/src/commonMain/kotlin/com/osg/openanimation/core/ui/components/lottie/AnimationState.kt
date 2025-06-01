package com.osg.openanimation.core.ui.components.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import io.github.alexzhirkevich.compottie.LottieCompositionResult
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import com.osg.openanimation.core.ui.generated.resources.Res
import com.osg.openanimation.core.ui.home.domain.AnimationUiData

@Immutable
data class AnimationDataState(
    val hash: String,
    val fetchAnimation: suspend () -> ByteArray,
){
    companion object {
        fun fromResource(
            resourcePath: String,
        ): AnimationDataState {
            return AnimationDataState(resourcePath){
                Res.readBytes(resourcePath)
            }
        }
    }
}

fun AnimationContentLoader.toAnimationDataState(
    animationMeta: AnimationMetadata,
): AnimationUiData {
    return AnimationUiData(
        animationState = AnimationDataState(animationMeta.hash) {
            fetchAnimationByPath(animationMeta.localFileName)
        },
        metadata = animationMeta,
    )

}

val AnimationDataState.compositionResult: LottieCompositionResult
    @Composable
    get() = rememberLottieComposition(hash) {
        LottieCompositionSpec.JsonString(
            fetchAnimation().decodeToString()
        )
    }
