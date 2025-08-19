package com.osg.openanimation.core.ui.color.model

import com.osg.openanimation.core.ui.color.util.findDeepPathsByKey
import com.osg.openanimation.core.ui.color.util.findElementByPath
import com.osg.openanimation.core.ui.color.util.replaceDeepChild
import com.osg.openanimation.core.ui.color.util.toColorStopsVector
import com.osg.openanimation.core.ui.color.util.toFloatArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement


suspend fun extractColorsFromJson(jsonString: String): List<PaintNode> {
    return withContext(Dispatchers.Default) {
        val jsonElement = Json.parseToJsonElement(jsonString)
        jsonElement.extractColors() + jsonElement.extractGradients()
    }
}

suspend fun updateColorsInJson(
    jsonString: String,
    colors: List<PaintNode>,
    transformOptions: TransformOptions,
): String {
    return withContext(Dispatchers.Default) {
        var jsonElement = Json.parseToJsonElement(jsonString)
        colors.forEach {
            jsonElement = jsonElement.replaceColor(it, transformOptions)
        }

        return@withContext Json.encodeToString(jsonElement)
    }
}

private fun replaceColorFill(
    paintNode: PaintNode.ColorFill,
    transformOptions: TransformOptions
): JsonElement {
    val newChild = when (paintNode.node) {
        is ColorProperty.Animated -> {
            paintNode.node.copy(
                k = paintNode.node.k.map { keyFrame ->
                    keyFrame.copy(
                        s = transformOptions.transform(keyFrame.s.toColor()!!)
                            .toFloatArray(),
                        e = keyFrame.e?.toColor()
                            ?.let { transformOptions.transform(it).toFloatArray() }
                            ?: keyFrame.e
                    )
                }
            )
        }
        is ColorProperty.Static -> {
            paintNode.node.copy(
                k = transformOptions.transform(paintNode.node.k.toColor()!!).toFloatArray()
            )
        }
    }
    return Json.encodeToJsonElement(newChild)
}

private fun replaceGradient(
    paintNode: PaintNode.Gradient,
    transformOptions: TransformOptions
): JsonElement {
    val k = paintNode.node.k
    val newChild = when (k) {
        is GradientStops.Animated -> {
            val newTransformedStops = paintNode.node.k.colorStops.map { colorStops ->
                colorStops.map { colorStop ->
                    colorStop.copy(
                        color = transformOptions.transform(colorStop.color)
                    )
                }
            }
            paintNode.node.copy(
                k = paintNode.node.k.copy(
                    k = paintNode.node.k.k.zip(newTransformedStops) { it, stops ->
                        it.copy(
                            s = stops.toColorStopsVector()
                        )
                    }
                )
            )
        }
        is GradientStops.Static -> {
            val transformedStops = paintNode.node.k.colorStops.map {
                it.copy(color = transformOptions.transform(it.color))
            }
            paintNode.node.copy(
                k = paintNode.node.k.copy(
                    k = transformedStops.toColorStopsVector()
                )
            )
        }
    }
    return Json.encodeToJsonElement(newChild)
}

fun JsonElement.replaceColor(
    paintNode: PaintNode,
    transformOptions: TransformOptions
): JsonElement {
    val jsonElement = when (paintNode) {
        is PaintNode.ColorFill -> replaceColorFill(paintNode, transformOptions)
        is PaintNode.Gradient -> replaceGradient(paintNode, transformOptions)
    }
    return replaceDeepChild(
        parts = paintNode.path.parts.map { it.key },
        newChild = jsonElement
    )
}

fun JsonElement.extractGradients(): List<PaintNode> {
    val paths = findDeepPathsByKey("g")
    return paths.mapNotNull { pathParts ->
        val element = findElementByPath(pathParts) ?: return@mapNotNull null
        val property = try {
            Json.decodeFromJsonElement<GradientProperty>(element)
        } catch (e: Exception) {
            return@mapNotNull null
        }
        val color = when (property.k) {
            is GradientStops.Animated -> {
                property.k.colorStops.flatten()
            }

            is GradientStops.Static -> {
                property.k.colorStops
            }
        }.map { it.color }

        val name = pathParts.parts
            .mapNotNull { it.name }
            .distinct()
            .joinToString(" > ")
            .ifEmpty { "Unnamed Color" }

        PaintNode.Gradient(path = pathParts, color = color, node = property, name = name)
    }
}


fun JsonElement.extractColors(): List<PaintNode> {
    val paths = findDeepPathsByKey("c")
    return paths.mapNotNull { pathParts ->
        val element = findElementByPath(pathParts) ?: return@mapNotNull null
        val property = try {
            Json.decodeFromJsonElement<ColorProperty>(element)
        } catch (e: Exception) {
            return@mapNotNull null
        }
        val colors = when (property) {
            is ColorProperty.Animated -> {
                property.k.mapNotNull { it.s.toColor() }
            }

            is ColorProperty.Static -> {
                listOfNotNull(property.k.toColor())
            }
        }
        val name = pathParts.parts
            .mapNotNull { it.name }
            .distinct()
            .joinToString(" > ")
            .ifEmpty { "Unnamed Color" }

        PaintNode.ColorFill(path = pathParts, color = colors, node = property, name = name)
    }
}

