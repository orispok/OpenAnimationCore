package com.osg.openanimation.core.ui.color

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

data class PathParts(
    val parts: List<LottiePart>,
){
    companion object
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