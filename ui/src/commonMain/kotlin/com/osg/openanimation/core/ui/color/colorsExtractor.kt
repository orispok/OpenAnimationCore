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

data class StaticColorNode(
    val path: PathParts,
    val color: List<Color>,
    val node: ColorProperty,
    val name: String,
)


suspend fun extractColorsFromJson(jsonString: String): List<StaticColorNode> {
    return withContext(Dispatchers.Default) {
        val jsonElement = Json.parseToJsonElement(jsonString)
        jsonElement.extractColors()
    }
}

suspend fun updateColorsInJson(
    jsonString: String,
    colors: List<StaticColorNode>,
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
    staticColorNode: StaticColorNode,
    transformOptions: TransformOptions
): JsonElement {
    val colorVecPath = staticColorNode.path.parts
    val newChild = when (staticColorNode.node) {
        is ColorProperty.Animated -> {
            staticColorNode.node.copy(
                k = staticColorNode.node.k.map { keyFrame ->
                    keyFrame.copy(
                        s = transformOptions.transform(keyFrame.s.toColor()!!).toFloatArray(),
                        e = keyFrame.e?.toColor()?.let { transformOptions.transform(it).toFloatArray() } ?: keyFrame.e
                    )
                }
            )

        }
        is ColorProperty.Static -> {
            staticColorNode.node.copy(
                k = transformOptions.transform(staticColorNode.node.k.toColor()!!).toFloatArray()
            )
        }
    }

    val jsonElement = Json.encodeToJsonElement(newChild)
    return replaceDeepChild(
        parts = colorVecPath.map { it.key },
        newChild = jsonElement
    )
}

fun JsonElement.extractColors(): List<StaticColorNode> {
    val paths = findDeepPathsByKey("c")
    return paths.mapNotNull { pathParts ->
        val element = findElementByPath(pathParts) ?: return@mapNotNull null
        val property = try {
            Json.decodeFromJsonElement<ColorProperty>(element)
        } catch (e: Exception) {
            return@mapNotNull null
        }
        val colors = when(property) {
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
        StaticColorNode(path = pathParts, color = colors, node = property, name = name)
    }
}


