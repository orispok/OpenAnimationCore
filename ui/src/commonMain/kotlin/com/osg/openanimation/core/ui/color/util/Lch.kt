package com.osg.openanimation.core.ui.color.util

import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class Lch (
    val l: Float, // Lightness (0-100)
    var c: Float, // Chroma (0-150+)
    var h: Float  // Hue in degrees (0-360)
) {
    fun shiftHue(degrees: Float) {
        h = (h + degrees) % 360f
    }

    fun shiftChroma(amount: Float) {
        c = (c + amount) % 150f
    }


    fun toColor(): Color {
        // Convert LCH to LAB
        val hRadians = h.toDouble() * PI / 180.0
        val labA = c.toDouble() * cos(hRadians)
        val labB = c.toDouble() * sin(hRadians)

        // Convert LAB to XYZ
        val fy = (l.toDouble() + 16.0) / 116.0
        val fx = labA / 500.0 + fy
        val fz = fy - labB / 200.0

        val xr = if (fx.pow(3.0) > 0.008856) fx.pow(3.0) else (116.0 * fx - 16.0) / 903.3
        val yr = if (l.toDouble() > 8.0) fy.pow(3.0) else l.toDouble() / 903.3
        val zr = if (fz.pow(3.0) > 0.008856) fz.pow(3.0) else (116.0 * fz - 16.0) / 903.3

        // Reference white D65
        val x = xr * 95.047
        val y = yr * 100.0
        val z = zr * 108.883

        // Convert XYZ to RGB (sRGB)
        var red = x * 0.032406 + y * (-0.015372) + z * (-0.004986)
        var green = x * (-0.009689) + y * 0.018758 + z * 0.000415
        var blue = x * 0.000557 + y * (-0.002040) + z * 0.010570

        // Apply gamma correction
        red = if (red > 0.0031308) 1.055 * red.pow(1.0 / 2.4) - 0.055 else 12.92 * red
        green = if (green > 0.0031308) 1.055 * green.pow(1.0 / 2.4) - 0.055 else 12.92 * green
        blue = if (blue > 0.0031308) 1.055 * blue.pow(1.0 / 2.4) - 0.055 else 12.92 * blue

        return Color(
            red = red.coerceIn(0.0, 1.0).toFloat(),
            green = green.coerceIn(0.0, 1.0).toFloat(),
            blue = blue.coerceIn(0.0, 1.0).toFloat(),
            alpha = 1f
        )
    }

    companion object {
//        val hueRange: ClosedFloatingPointRange<Float> = 0f..360f
//        val chromaRange: ClosedFloatingPointRange<Float> = 0f..150f

        fun Color.toLch(): Lch {
            // Convert sRGB to linear RGB
            var red = this.red.toDouble()
            var green = this.green.toDouble()
            var blue = this.blue.toDouble()

            red = if (red > 0.04045) ((red + 0.055) / 1.055).pow(2.4) else red / 12.92
            green = if (green > 0.04045) ((green + 0.055) / 1.055).pow(2.4) else green / 12.92
            blue = if (blue > 0.04045) ((blue + 0.055) / 1.055).pow(2.4) else blue / 12.92

            // Convert RGB to XYZ (sRGB)
            val x = (red * 0.412453 + green * 0.357580 + blue * 0.180423) * 100.0
            val y = (red * 0.212671 + green * 0.715160 + blue * 0.072169) * 100.0
            val z = (red * 0.019334 + green * 0.119193 + blue * 0.950227) * 100.0

            // Convert XYZ to LAB
            val xr = x / 95.047
            val yr = y / 100.0
            val zr = z / 108.883

            val fx = if (xr > 0.008856) xr.pow(1.0 / 3.0) else (7.787 * xr + 16.0 / 116.0)
            val fy = if (yr > 0.008856) yr.pow(1.0 / 3.0) else (7.787 * yr + 16.0 / 116.0)
            val fz = if (zr > 0.008856) zr.pow(1.0 / 3.0) else (7.787 * zr + 16.0 / 116.0)

            val labL = 116.0 * fy - 16.0
            val labA = 500.0 * (fx - fy)
            val labB = 200.0 * (fy - fz)

            // Convert LAB to LCH
            val lchL = labL
            val lchC = sqrt(labA * labA + labB * labB)
            var lchH = atan2(labB, labA) * 180.0 / PI

            if (lchH < 0.0) {
                lchH += 360.0
            }

            return Lch(
                l = lchL.toFloat(),
                c = lchC.toFloat(),
                h = lchH.toFloat()
            )
        }
    }
}