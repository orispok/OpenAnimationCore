package com.osg.openanimation.core.ui.components.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import io.github.alexzhirkevich.compottie.LottieCompositionResult
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import com.osg.openanimation.core.ui.generated.resources.Res

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


val AnimationDataState.compositionResult: LottieCompositionResult
    @Composable
    get() = rememberLottieComposition(hash) {
        LottieCompositionSpec.JsonString(
            fetchAnimation().decodeToString()
        )
    }
