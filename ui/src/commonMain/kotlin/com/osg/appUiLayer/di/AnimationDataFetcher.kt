package com.osg.appUiLayer.di

fun interface AnimationDataFetcher {
    suspend fun fetchAnimationByPath(path: String): ByteArray
}