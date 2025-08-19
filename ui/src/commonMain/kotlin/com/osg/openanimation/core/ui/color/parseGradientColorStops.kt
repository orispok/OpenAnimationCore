package com.osg.openanimation.core.ui.color
import androidx.compose.ui.graphics.Color

/**
 * Represents a single color stop in a gradient, containing an offset and a color.
 *
 * @property offset The position of the color in the gradient, from 0.0 to 1.0.
 * @property color The color at that offset.
 */
data class ColorStop(val offset: Float, val color: Color)

/**
 * Parses a flat list of floats into a list of color stops for a Jetpack Compose gradient.
 *
 * The function supports two formats for the input list:
 * 1.  **RGB only**: A sequence of [offset, r, g, b] for each color.
 * Example: [0.0f, 0.1f, 0.2f, 0.3f, 1.0f, 0.4f, 0.5f, 0.6f]
 *
 * 2.  **RGB with Alpha**: The RGB sequence is followed by an alpha sequence,
 * which consists of repeating offsets and their corresponding alpha values.
 * Format: [offset, r, g, b, ...] followed by [offset, a, offset, a, ...]
 * Example: [0.0f, 0.1f, 0.2f, 0.3f, 1.0f, 0.4f, 0.5f, 0.6f, 0.0f, 0.5f, 1.0f, 1.0f]
 *
 * @return A list of [ColorStop] objects, sorted by offset. This list can be used with
 * Jetpack Compose Brush functions (e.g., `Brush.linearGradient`).
 */
fun List<Float>.parseGradientColorStops(): List<ColorStop> {
    if (this.isEmpty()) {
        return emptyList()
    }

    // A map to store the colors with their offsets. Using a map handles cases
    // where alpha values for the same offset might appear later.
    val colorStopsMap = mutableMapOf<Float, Color>()

    // --- 1. Find the split point between color data and alpha data ---
    // The alpha data section starts when an offset is repeated.
    var splitIndex = this.size
    val seenOffsets = mutableSetOf<Float>()

    // We iterate in chunks of 4, as the color data is [offset, r, g, b].
    for (i in this.indices step 4) {
        // Ensure there are enough elements for a full color definition.
        if (i + 3 >= this.size) {
            // If not, we've likely hit the end of the color section or have malformed data.
            // In either case, the rest of the array cannot be color data.
            splitIndex = i
            break
        }

        val offset = this[i]
        if (seenOffsets.contains(offset)) {
            // We found a repeated offset, which marks the beginning of the alpha section.
            splitIndex = i
            break
        }
        seenOffsets.add(offset)
    }

    // --- 2. Parse the color data (RGB) ---
    // The color data is the part of the list before the split index.
    val colorData = this.subList(0, splitIndex)
    for (i in colorData.indices step 4) {
        // Check again for a complete color chunk.
        if (i + 3 < colorData.size) {
            val offset = colorData[i]
            val r = colorData[i + 1]
            val g = colorData[i + 2]
            val b = colorData[i + 3]
            // Initially, all colors are assumed to be fully opaque.
            colorStopsMap[offset] = Color(red = r, green = g, blue = b, alpha = 1.0f)
        }
    }

    // --- 3. Parse the alpha data, if it exists ---
    if (splitIndex < this.size) {
        val alphaData = this.subList(splitIndex, this.size)
        // Alpha data comes in pairs of [offset, alpha].
        for (i in alphaData.indices step 2) {
            if (i + 1 < alphaData.size) {
                val offset = alphaData[i]
                val alpha = alphaData[i + 1]

                // Find the color that was already parsed for this offset.
                val existingColor = colorStopsMap[offset]
                if (existingColor != null) {
                    // Update the color with its new alpha value.
                    colorStopsMap[offset] = existingColor.copy(alpha = alpha)
                }
            }
        }
    }

    // --- 4. Return the final list, sorted by offset ---
    // Convert the map to a list of ColorStop objects and sort by the offset.
    return colorStopsMap.map { (offset, color) -> ColorStop(offset, color) }.sortedBy { it.offset }
}


fun List<ColorStop>.toColorStopsVector(): List<Float> {
    if (this.isEmpty()) {
        return emptyList()
    }

    val result = mutableListOf<Float>()
    val alphaData = mutableListOf<Float>()
    var hasTransparency = false

    // First, build the color part of the vector and check for transparency.
    for (stop in this) {
        result.add(stop.offset)
        result.add(stop.color.red)
        result.add(stop.color.green)
        result.add(stop.color.blue)

        if (stop.color.alpha < 1.0f) {
            hasTransparency = true
        }
        // Always build the alpha data list in case it's needed.
        alphaData.add(stop.offset)
        alphaData.add(stop.color.alpha)
    }

    // If any color has transparency, append the alpha data to the result.
    if (hasTransparency) {
        result.addAll(alphaData)
    }

    return result
}