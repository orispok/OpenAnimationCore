package com.osg.appUiLayer.di

import androidx.compose.runtime.compositionLocalOf
import com.osg.appUiLayer.graph.Destination

data class AppLinkProvider(
    private val hostUrl: String = "http://localhost:8080"
){
    val termUrl: String = "$hostUrl/term"
    val privacyUrl: String = "$hostUrl/privacy"

    fun windowUrl(destination: Destination): String {
        val destinationPath = when (destination) {
            is Destination.Home -> {
                val serialName = Destination.Home.serializer().descriptor.serialName
                "$serialName/${destination.searchQuerySerial}"
            }
            is Destination.AnimationDetails -> {
                val serialName = Destination.AnimationDetails.serializer().descriptor.serialName
                "$serialName/${destination.hash}"
            }
        }
        return "$hostUrl/$destinationPath"
    }
}

val LocalLinkProvider = compositionLocalOf { (AppLinkProvider()) }
