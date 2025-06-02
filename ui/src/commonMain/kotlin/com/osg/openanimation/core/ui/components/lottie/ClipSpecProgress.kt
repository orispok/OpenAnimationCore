package com.osg.openanimation.core.ui.components.lottie

import io.github.alexzhirkevich.compottie.LottieClipSpec

data class ClipSpecProgress(
    val min: Float,
    val max: Float
){
    internal val lottieClipSpec = LottieClipSpec.Progress(min, max)
}