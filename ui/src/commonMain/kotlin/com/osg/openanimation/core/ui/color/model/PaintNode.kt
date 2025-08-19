package com.osg.openanimation.core.ui.color.model

import androidx.compose.ui.graphics.Color
import com.osg.openanimation.core.ui.color.util.PathParts

sealed interface PaintNode {
    val path: PathParts
    val color: List<Color>
    val name: String

    data class ColorFill(
        override val path: PathParts,
        override val color: List<Color>,
        val node: ColorProperty,
        override val name: String,
    ) : PaintNode

    data class Gradient(
        override val path: PathParts,
        override val name: String,
        val node: GradientProperty,
        override val color: List<Color>
    ) : PaintNode
}