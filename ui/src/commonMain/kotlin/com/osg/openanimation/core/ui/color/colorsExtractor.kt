package com.osg.openanimation.core.ui.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class LottieProperty(
    val a: Int? = null,
    val k: JsonElement,
    val ix: Int? = null
)


data class LottiePart(
    val key: String,
    val name: String? = null,
)

data class PathParts(
    val parts: List<LottiePart>,
){
    companion object
}

data class StaticColorNode(
    val path: PathParts,
    val color: Color,
    val name: String,
)

fun LottieProperty.staticColorOrNull(): Color? {
    if (a != 0) return null
    val arr = k as? JsonArray ?: return null
    val rgba = arr.map { it.jsonPrimitive.float }
    if (rgba.size != 4) return null
    return Color(
        red = rgba[0],
        green = rgba[1],
        blue = rgba[2],
        alpha = rgba[3],
        colorSpace = ColorSpaces.Srgb
    )
}

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

fun JsonElement.replaceColor(
    staticColorNode: StaticColorNode,
    transformOptions: TransformOptions
): JsonElement {
    val newColor = transformOptions.transform(staticColorNode.color)
    val newChild = buildJsonArray {
        add(newColor.red.roundTo(2))
        add(newColor.green.roundTo(2))
        add(newColor.blue.roundTo(2))
        add(newColor.alpha.roundTo(2))
    }

    val colorVecPath = staticColorNode.path.parts + LottiePart(key = "k", name = "Color")
    return replaceDeepChild(
        parts = colorVecPath.map { it.key },
        newChild = newChild
    )
}

fun JsonElement.extractColors(): List<StaticColorNode> {
    val paths = findDeepPathsByKey("c")
    return paths.mapNotNull { pathParts ->
        val element = findElementByPath(pathParts) ?: return@mapNotNull null
        val property = try {
            Json.decodeFromJsonElement<LottieProperty>(element)
        } catch (e: Exception) {
            return@mapNotNull null
        }
        val color = property.staticColorOrNull() ?: return@mapNotNull null
        val name = pathParts.parts
            .mapNotNull { it.name }
            .distinct()
            .joinToString(" > ")
            .ifEmpty { "Unnamed Color" }
        StaticColorNode(pathParts, color, name)
    }
}


fun JsonElement?.findDeepPathsByKey(key: String): List<PathParts> {
    val paths = mutableListOf<PathParts>()

    fun find(element: JsonElement?, currentPath: List<LottiePart>) {
        when (element) {
            is JsonObject -> {
                val nm = element["nm"]?.jsonPrimitive?.contentOrNull
                for ((k, v) in element) {
                    val newPath = currentPath + LottiePart(key = k, name = nm)
                    if (k == key) {
                        paths.add(PathParts(newPath))
                    }
                    find(v, newPath)
                }
            }
            is JsonArray -> {
                element.forEachIndexed { index, item ->
                    val newPath = currentPath + LottiePart(key = index.toString())
                    find(item, newPath)
                }
            }
            else -> {
                // Primitives or null, do nothing
            }
        }
    }

    find(this, emptyList())
    return paths
}

fun JsonElement?.findElementByPath(pathParts: PathParts): JsonElement? {
    // Start from the current element and walk through the path parts
    var current: JsonElement? = this
    if (current == null) return null

    for (part in pathParts.parts) {
        current = when (val node = current) {
            is JsonObject -> {
                // Navigate by key in object
                node[part.key]
            }
            is JsonArray -> {
                // Navigate by numeric index in array
                val idx = part.key.toIntOrNull() ?: return null
                if (idx in 0 until node.size) node[idx] else return null
            }
            else -> return null // Reached a primitive/null before finishing the path
        }

        if (current == null) return null
    }

    return current
}

fun JsonElement?.replaceChild(childName: String, newChild: JsonElement): JsonElement {
    return when (this) {
        is JsonObject -> {
            val newMap = this.toMutableMap()
            newMap[childName] = newChild
            JsonObject(newMap)
        }
        is JsonArray -> {
            JsonArray(
                this.toMutableList().apply {
                    val index = childName.toInt()
                    this[index] = newChild
                }
            )
        }
        else -> {
            buildJsonObject {
                put(childName, newChild)
            }
        }
    }
}

//     A                        A
//    /  \                     /  \
//    B1  B2      == >;        B1  B2
//    /    \                   /    \
//   C3     C4                C3     C4*
fun JsonElement?.replaceDeepChild(parts: List<String>, newChild: JsonElement): JsonElement {
    return if (parts.size == 1) {
        replaceChild(parts.first(), newChild)
    } else {
        val child = when (this) {
            is JsonObject -> this[parts.first()]
            is JsonArray -> this[parts.first().toInt()]
            else -> null
        }
        replaceChild(
            childName = parts.first(),
            newChild = child.replaceDeepChild(parts.drop(1), newChild)
        )
    }
}