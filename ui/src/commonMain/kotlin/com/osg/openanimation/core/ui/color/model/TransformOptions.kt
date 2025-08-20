package com.osg.openanimation.core.ui.color.model

import androidx.compose.ui.graphics.Color
import com.osg.openanimation.core.ui.color.util.toHsl

data class TransformOptions(
    val hueShift: Float = 0f,
    val lightnessShift: Float = 0f,
) {
    fun transform(color: Color): Color {
        val hslColor = color.toHsl()
        return Color.Companion.hsl(
            hue = (hslColor.hue + hueShift) % 360f,
            saturation = hslColor.saturation,
            lightness = (hslColor.lightness + lightnessShift) % 1f,
            alpha = color.alpha
        )
    }

    fun transform(colors: List<Color>): List<Color> {
        return colors.map { color ->
            transform(color)
        }
    }
}