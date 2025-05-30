package com.osg.appUiLayer.components.lottie

import io.github.alexzhirkevich.compottie.LottieClipSpec

data class ClipSpecProgress(
    val min: Float,
    val max: Float
){
    internal val lottieClipSpec = LottieClipSpec.Progress(min, max)
}