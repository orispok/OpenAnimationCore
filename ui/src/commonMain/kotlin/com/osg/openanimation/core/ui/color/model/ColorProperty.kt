package com.osg.openanimation.core.ui.color.model

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
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive


@Serializable(with = ColorPropertySerializer::class) // Point to the custom serializer here
sealed interface ColorProperty {
    val a: Int?
    val ix: Int?
    val x: JsonElement?
    val sid: JsonElement?

    @Serializable
    data class Static(
        val k: List<Float>,
        override val a: Int?,
        override val ix: Int?,
        override val x: JsonElement?,
        override val sid: JsonElement?,
    ) : ColorProperty

    @Serializable
    data class Animated(
        val k: List<ColorKeyFrame>,
        override val a: Int?,
        override val ix: Int?,
        override val x: JsonElement?,
        override val sid: JsonElement?
    ) : ColorProperty
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
        element<Int?>("a", isOptional = true)
        element<Int?>("ix", isOptional = true)
        element<JsonElement?>("x", isOptional = true)
        element<JsonElement?>("sid", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: ColorProperty) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw SerializationException("ColorPropertySerializer can only be used with JSON")



        val finalObject = buildJsonObject {
            value.a?.let { put("a", JsonPrimitive(it)) }
            value.ix?.let { put("ix", JsonPrimitive(it)) }
            value.x?.let { put("x", it) }
            value.sid?.let { put("sid", it) }
            when (value) {
                is ColorProperty.Static -> {
                    put("k", JsonArray(value.k.map { JsonPrimitive(it) }))
                }

                is ColorProperty.Animated -> {
                    put(
                        "k",
                        JsonArray(value.k.map {
                            jsonEncoder.json.encodeToJsonElement(
                                ColorKeyFrame.serializer(),
                                it
                            )
                        })
                    )
                }
            }
        }
        jsonEncoder.encodeJsonElement(finalObject)
    }

    override fun deserialize(decoder: Decoder): ColorProperty {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("ColorPropertySerializer can only be used with JSON")

        val element = jsonDecoder.decodeJsonElement()
        if (element !is JsonObject) throw SerializationException("Expected JsonObject for ColorProperty")

        val k = element["k"] ?: throw SerializationException("Missing 'k' field in ColorProperty")

        return when (k) {
            is JsonArray -> {
                // It's safer to check for an empty array first.
                // An empty keyframe array should result in an Animated type.
                if (k.isEmpty() || k.first() is JsonObject) {
                    // animated
                    val frames = k.map {
                        jsonDecoder.json.decodeFromJsonElement(
                            ColorKeyFrame.serializer(),
                            it
                        )
                    }
                    ColorProperty.Animated(
                        frames,
                        a = element["a"]?.jsonPrimitive?.intOrNull,
                        ix = element["ix"]?.jsonPrimitive?.intOrNull,
                        x = element["x"],
                        sid = element["sid"]
                    )
                } else {
                    // static
                    val floats = k.map { it.jsonPrimitive.float }
                    ColorProperty.Static(
                        k = floats,
                        a = element["a"]?.jsonPrimitive?.intOrNull,
                        ix = element["ix"]?.jsonPrimitive?.intOrNull,
                        x = element["x"],
                        sid = element["sid"]
                    )
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