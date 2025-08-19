package com.osg.openanimation.core.ui.color.model

import androidx.compose.ui.graphics.Color
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState

data class ColorsEditPalette(
    val options: List<List<Color>> = listOf(
        listOf(
            Color(0xFF000000),
            Color(0xFFFFFFFF),
            Color(0xFFFF0000),
            Color(0xFF00FF00),
        )
    ),
    val processedJsonState: AnimationDataState = AnimationDataState.Processing,
    val expanded: Boolean = false,
)