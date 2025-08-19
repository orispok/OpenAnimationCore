package com.osg.openanimation.core.ui.color.model

import com.osg.openanimation.core.ui.color.util.parseGradientColorStops
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
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = GradientStopsSerializer::class)
sealed interface GradientStops{
    val a: Int?
    @Serializable
    data class Static(
        val k: List<Float>,
        override val a: Int?,
    ) : GradientStops{

        val colorStops by lazy {
            k.parseGradientColorStops()
        }
    }

    @Serializable
    data class Animated(
        val k: List<GradientKeyFrame>,
        val ix: Int? =  null,
        override val a: Int?,
    ) : GradientStops{
        val colorStops by lazy {
            k.map { it.s.parseGradientColorStops() }
        }
    }
}

@Serializable
data class GradientProperty(
    val ix: Int? =  null,
    val x: JsonElement? =  null,
    val sid: JsonElement? =  null,
    val p: Int?  =  null,
    val k: GradientStops,
)

@Serializable
data class GradientKeyFrame(
    val t: JsonElement? = null, // Time - Frame number
    val h: JsonElement? = null, // Hold - 0-1 integer
    val i: JsonElement? = null, // In Tangent - Keyframe Easing
    val o: JsonElement? = null, // Out Tangent - Keyframe Easing
    val s: List<Float>, // Value - Gradient at this keyframe
    val e: List<Float>? = null, // End value - Gradient at the end of keyframe (deprecated)
)

object GradientStopsSerializer : KSerializer<GradientStops> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("GradientStops") {
            element<JsonElement>("k")
        }

    override fun serialize(encoder: Encoder, value: GradientStops) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw SerializationException("GradientStopsSerializer only works with JSON")

        val jsonObject = when (value) {
            is GradientStops.Static -> buildJsonObject {
                put("k", JsonArray(value.k.map { JsonPrimitive(it) }))
                value.a?.let { put("a", JsonPrimitive(it)) }
            }
            is GradientStops.Animated -> buildJsonObject {
                value.a?.let { put("a", JsonPrimitive(it)) }
                put(
                    "k",
                    JsonArray(value.k.map {
                        jsonEncoder.json.encodeToJsonElement(
                            GradientKeyFrame.serializer(),
                            it
                        )
                    })
                )
                value.ix?.let { put("ix", JsonPrimitive(it)) }
            }
        }

        jsonEncoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): GradientStops {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("GradientStopsSerializer only works with JSON")

        val element = jsonDecoder.decodeJsonElement()
        if (element !is JsonObject) throw SerializationException("Expected JsonObject for GradientStops")

        val k = element["k"] ?: throw SerializationException("Missing 'k' field in GradientStops")

        return when (k) {
            is JsonArray -> {
                if (k.firstOrNull() is JsonObject) {
                    // Animated
                    val frames = k.map { jsonDecoder.json.decodeFromJsonElement(GradientKeyFrame.serializer(), it) }
                    GradientStops.Animated(
                        k = frames,
                        a = element["a"]?.jsonPrimitive?.int,
                        ix = element["ix"]?.jsonPrimitive?.int
                    )
                } else {
                    // Static
                    val floats = k.map { it.jsonPrimitive.float }
                    GradientStops.Static(
                        k = floats,
                        a = element["a"]?.jsonPrimitive?.int
                    )
                }
            }
            else -> throw SerializationException("Unexpected 'k' format in GradientStops")
        }
    }
}