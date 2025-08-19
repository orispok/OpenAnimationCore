package com.osg.core.ui.details

import com.osg.openanimation.core.ui.color.LottiePart
import com.osg.openanimation.core.ui.color.PathParts
import com.osg.openanimation.core.ui.color.findElementByPath
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class TransformDeepByKeyTest {
    @Test
    fun testFindElementByPathInArray() {
        val json = buildJsonObject {
            put("users", buildJsonArray {
                add(buildJsonObject { put("name", JsonPrimitive("John")) })
                add(buildJsonObject { put("name", JsonPrimitive("Alice")) })
            })
        }
        val path0 = PathParts(listOf("users", "0", "name"))
        val path1 = PathParts(listOf("users", "1", "name"))
        assertEquals(JsonPrimitive("John"), json.findElementByPath(path0))
        assertEquals(JsonPrimitive("Alice"), json.findElementByPath(path1))
    }

    @Test
    fun testFindElementByPathInvalidPath() {
        val json = buildJsonObject {
            put("user", buildJsonObject { put("name", JsonPrimitive("John")) })
        }
        // Non-existent key
        assertEquals(null, json.findElementByPath(PathParts(listOf("user", "age"))))
        // Non-numeric index in array
        val arr = buildJsonObject {
            put("items", buildJsonArray { add(JsonPrimitive(1)); add(JsonPrimitive(2)) })
        }
        assertEquals(null, arr.findElementByPath(PathParts(listOf("items", "one"))))
        // Index out of bounds
        assertEquals(null, arr.findElementByPath(PathParts(listOf("items", "2"))))
        // Primitive before finishing path
        assertEquals(null, JsonPrimitive("x").findElementByPath(PathParts(listOf("foo"))))
    }

    @Test
    fun testFindElementByPathNullRoot() {
        val nullJson: JsonElement? = null
        val result = nullJson.findElementByPath(PathParts(listOf("any")))
        assertEquals(null, result)
    }
}

operator fun PathParts.Companion.invoke(parts: List<String>): PathParts {
    return PathParts(parts.map { LottiePart(it) })
}