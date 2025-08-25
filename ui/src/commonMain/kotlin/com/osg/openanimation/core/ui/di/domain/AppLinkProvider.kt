package com.osg.openanimation.core.ui.di.domain

import androidx.compose.runtime.compositionLocalOf
import com.osg.openanimation.core.ui.graph.Dashboard
import com.osg.openanimation.core.ui.graph.Destination
import com.osg.openanimation.core.ui.graph.EditAnimation


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

            Dashboard -> Dashboard.serializer().descriptor.serialName
            is EditAnimation -> {
                val serialName = EditAnimation.serializer().descriptor.serialName
                "$serialName/${destination.animationId}"
            }
        }
        return "$hostUrl/#$destinationPath"
    }
}

val LocalLinkProvider = compositionLocalOf { (AppLinkProvider()) }
