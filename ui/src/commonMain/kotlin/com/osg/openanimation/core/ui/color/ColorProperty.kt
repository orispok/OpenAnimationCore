package com.osg.openanimation.core.ui.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonPrimitive


@Serializable(with = ColorPropertySerializer::class) // Point to the custom serializer here
sealed interface ColorProperty {
    val a: Int?
        get() = null
    val ix: Int?
        get() = null
    val x: JsonElement?
        get() = null
    val sid: JsonElement?
        get() = null

    @Serializable
    data class Static(val k: List<Float>) : ColorProperty

    @Serializable
    data class Animated(val k: List<ColorKeyFrame>) : ColorProperty
}

@Serializable
data class ColorKeyFrame(
    val t: JsonElement? = null,
    val h: JsonElement? = null,
    val i: JsonElement? = null,
    val o: JsonElement? = null,
    val s: List<Float>,
    val e: List<Float>? = null,
)



object ColorPropertySerializer : KSerializer<ColorProperty> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ColorProperty") {
        element<JsonElement>("k")
    }

    override fun serialize(encoder: Encoder, value: ColorProperty) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw SerializationException("ColorPropertySerializer can only be used with JSON")

        val jsonObject = when (value) {
            is ColorProperty.Static -> buildJsonObject {
                put("k", JsonArray(value.k.map { JsonPrimitive(it) }))
            }
            is ColorProperty.Animated -> buildJsonObject {
                put("k", JsonArray(value.k.map { jsonEncoder.json.encodeToJsonElement(ColorKeyFrame.serializer(), it) }))
            }
        }
        jsonEncoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): ColorProperty {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("ColorPropertySerializer can only be used with JSON")

        val element = jsonDecoder.decodeJsonElement()
        if (element !is JsonObject) throw SerializationException("Expected JsonObject for ColorProperty")

        val k = element["k"] ?: throw SerializationException("Missing 'k' field in ColorProperty")

        return when (k) {
            is JsonArray -> {
                if (k.firstOrNull() is JsonObject) {
                    // animated
                    val frames = k.map { jsonDecoder.json.decodeFromJsonElement(ColorKeyFrame.serializer(), it) }
                    ColorProperty.Animated(frames)
                } else {
                    // static
                    val floats = k.map { it.jsonPrimitive.float }
                    ColorProperty.Static(floats)
                }
            }
            else -> throw SerializationException("Unexpected 'k' format in ColorProperty")
        }
    }
}



fun List<Float>.toColor(): Color? {
    if (size != 4) return null
    return Color(
        red = this[0],
        green = this[1],
        blue = this[2],
        alpha = this[3],
        colorSpace = ColorSpaces.Srgb
    )
}