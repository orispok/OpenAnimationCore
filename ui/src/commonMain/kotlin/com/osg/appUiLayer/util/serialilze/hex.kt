package com.osg.appUiLayer.util.serialilze

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromHexString
import kotlinx.serialization.encodeToHexString
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> T.toHexString(): String {
    return ProtoBuf.Default.encodeToHexString(this)
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> String.decodeFromHexString(): T {
    return ProtoBuf.Default.decodeFromHexString<T>(this)
}