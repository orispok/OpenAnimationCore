package com.osg.openanimation.core.ui.color.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ColorsEditPalette(
    val options: ImmutableList<ImmutableList<Color>> = persistentListOf(),
    val selectedOptionIndex: Int = 0,
    val processedJsonState: AnimationDataState = AnimationDataState.Processing,
)