package com.osg.openanimation.core.ui.color

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement


data class LottiePart(
    val key: String,
    val name: String? = null,
)

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

fun Color.toFloatArray(): List<Float> {
    return listOf(
        red.roundTo(2),
        green.roundTo(2),
        blue.roundTo(2),
        alpha.roundTo(2)
    )
}

fun JsonElement.replaceColor(
    paintNode: PaintNode,
    transformOptions: TransformOptions
): JsonElement {
    val colorVecPath = paintNode.path.parts
    return when (paintNode) {
        is PaintNode.ColorFill -> {
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

            val jsonElement = Json.encodeToJsonElement(newChild)
            replaceDeepChild(
                parts = colorVecPath.map { it.key },
                newChild = jsonElement
            )
        }

        is PaintNode.Gradient -> {
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

            val jsonElement = Json.encodeToJsonElement(newChild)
            replaceDeepChild(
                parts = colorVecPath.map { it.key },
                newChild = jsonElement
            )
        }
    }
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


