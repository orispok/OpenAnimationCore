package com.osg.openanimation.core.ui.color.model

import androidx.compose.ui.graphics.Color
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState

data class ColorsEditPalette(
    val options: List<List<Color>> = emptyList(),
    val selectedOptionIndex: Int = 0,
    val processedJsonState: AnimationDataState = AnimationDataState.Processing,
)