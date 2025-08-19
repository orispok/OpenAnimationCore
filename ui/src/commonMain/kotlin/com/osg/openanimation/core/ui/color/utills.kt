package com.osg.openanimation.core.ui.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.fastCoerceIn
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class HslColor(
    val hue: Float,
    val saturation: Float,
    val lightness: Float,
    val alpha: Float = 1f,
) {
    val color = Color.hsl(hue, saturation, lightness, alpha)
}


fun Color.toHsl(): HslColor {
    val max = max(red, max(green, blue))
    val min = min(red, min(green, blue))
    val deltaMaxMin = max - min

    val l = (max + min) / 2f

    val h: Float
    val s: Float

    if (max == min) {
        // Monochromatic
        h = 0f
        s = 0f
    } else {
        s = deltaMaxMin / (1f - abs(2f * l - 1f))
        var hue = when (max) {
            red -> (green - blue) / deltaMaxMin
            green -> (blue - red) / deltaMaxMin + 2f
            else -> (red - green) / deltaMaxMin + 4f
        }
        hue *= 60f
        if (hue < 0) {
            hue += 360f
        }
        h = hue
    }

    return HslColor(
        hue = h,
        saturation = s.fastCoerceIn(0f, 1f),
        lightness = l.fastCoerceIn(0f, 1f),
        alpha = alpha
    )
}


fun Color.toFloatArray(): List<Float> {
    return listOf(
        red.roundTo(2),
        green.roundTo(2),
        blue.roundTo(2),
        alpha.roundTo(2)
    )
}