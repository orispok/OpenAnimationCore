package com.osg.openanimation.core.ui.color

import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A data class to represent a color in the CIELAB color space.
 * CIELAB is designed to be perceptually uniform, meaning the geometric distance
 * between two colors in this space corresponds to their perceived difference.
 *
 * @property l The lightness component (0-100).
 * @property a The green-red component.
 * @property b The blue-yellow component.
 */
private data class LabColor(val l: Double, val a: Double, val b: Double)

/**
 * Finds a subset of `k` colors from the input list that are most visually distinct from each other.
 *
 * This function uses a greedy algorithm:
 * 1. It converts all `androidx.compose.ui.graphics.Color` objects to the CIELAB color space,
 * which is more perceptually uniform than RGB.
 * 2. It starts with the first color in the list.
 * 3. It iteratively adds the color from the remaining pool that has the greatest minimum
 * distance to the colors already selected. This maximizes the separation between colors.
 *
 * @param k The number of distinct colors to return.
 * @return A list containing the `k` most different colors. Returns an empty list if k is 0,
 * or the original list if k is greater than or equal to the list size.
 */
fun List<Color>.getKMostDifferentColors(k: Int): List<Color> {
    // Handle edge cases where no selection is needed or possible.
    if (k <= 0) return emptyList()
    if (k >= size) return distinct()

    // Create a mutable copy of the original colors to work with.
    val remainingColors = distinct().toMutableList()
    if (k >= remainingColors.size) return remainingColors

    // Convert all colors to the LAB color space for accurate difference calculation.
    val labColorMap = remainingColors.associateWith { it.toLab() }
    val remainingLabColors = labColorMap.values.toMutableList()

    // This list will store the final selected colors.
    val resultLabColors = mutableListOf<LabColor>()

    // Start by selecting the first color as the initial point of reference.
    val firstColor = remainingLabColors.removeFirst()
    resultLabColors.add(firstColor)

    // Iteratively select the remaining k-1 colors.
    while (resultLabColors.size < k && remainingLabColors.isNotEmpty()) {
        var maxMinDistance = -1.0
        var nextColorToAdd: LabColor? = null

        // Find the color in the remaining pool that is "farthest" from the selected set.
        for (candidateColor in remainingLabColors) {
            // The "distance" of a candidate is its minimum distance to any color already selected.
            val minDistanceToResultSet = resultLabColors.minOf { selectedColor ->
                calculateLabDistance(candidateColor, selectedColor)
            }

            // We want the candidate with the maximum minimum distance.
            if (minDistanceToResultSet > maxMinDistance) {
                maxMinDistance = minDistanceToResultSet
                nextColorToAdd = candidateColor
            }
        }

        // Add the best candidate found in this iteration.
        if (nextColorToAdd != null) {
            resultLabColors.add(nextColorToAdd)
            remainingLabColors.remove(nextColorToAdd)
        } else {
            // This should not happen if remainingLabColors is not empty, but break as a safeguard.
            break
        }
    }

    // Map the selected LAB colors back to their original Compose Color objects.
    return resultLabColors.mapNotNull { resultLab ->
        labColorMap.entries.find { it.value == resultLab }?.key
    }
}

/**
 * Calculates the Euclidean distance (Delta E) between two colors in the CIELAB space.
 */
private fun calculateLabDistance(lab1: LabColor, lab2: LabColor): Double {
    return sqrt((lab1.l - lab2.l).pow(2) + (lab1.a - lab2.a).pow(2) + (lab1.b - lab2.b).pow(2))
}

// --- Color Space Conversion Helpers ---

// D65 reference white point for sRGB.
private const val D65_X = 95.047
private const val D65_Y = 100.0
private const val D65_Z = 108.883

/**
 * Extension function to convert an `androidx.compose.ui.graphics.Color` (sRGB) to a `LabColor`.
 * The conversion follows the standard sRGB -> XYZ -> CIELAB path.
 */
private fun Color.toLab(): LabColor {
    // Step 1: Linearize sRGB values (gamma correction).
    fun linearize(value: Float): Double {
        return if (value > 0.04045f) {
            ((value + 0.055) / 1.055).pow(2.4)
        } else {
            (value / 12.92f).toDouble()
        }
    }

    val rLinear = linearize(this.red) * 100.0
    val gLinear = linearize(this.green) * 100.0
    val bLinear = linearize(this.blue) * 100.0

    // Step 2: Convert from sRGB to XYZ color space.
    val x = rLinear * 0.4124 + gLinear * 0.3576 + bLinear * 0.1805
    val y = rLinear * 0.2126 + gLinear * 0.7152 + bLinear * 0.0722
    val z = rLinear * 0.0193 + gLinear * 0.1192 + bLinear * 0.9505

    // Step 3: Convert from XYZ to CIELAB color space.
    var varX = x / D65_X
    var varY = y / D65_Y
    var varZ = z / D65_Z

    fun transform(v: Double): Double {
        return if (v > 0.008856) v.pow(1.0 / 3.0) else (7.787 * v) + (16.0 / 116.0)
    }

    varX = transform(varX)
    varY = transform(varY)
    varZ = transform(varZ)

    val l = (116.0 * varY) - 16.0
    val a = 500.0 * (varX - varY)
    val b = 200.0 * (varY - varZ)

    return LabColor(l, a, b)
}
