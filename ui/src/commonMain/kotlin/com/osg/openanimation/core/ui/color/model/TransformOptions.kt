package com.osg.openanimation.core.ui.color.model

import androidx.compose.ui.graphics.Color
import com.osg.openanimation.core.ui.color.util.Lch.Companion.toLch

data class TransformOptions(
    val hueShift: Float = 0f,
    val chromaShift: Float = 0f,
) {
    fun transform(color: Color): Color {
        val lchColor = color.toLch().apply {
            shiftHue(hueShift)
            shiftChroma(chromaShift)
        }
        return lchColor.toColor()
    }

    fun transform(colors: List<Color>): List<Color> {
        return colors.map { color ->
            transform(color)
        }
    }
}