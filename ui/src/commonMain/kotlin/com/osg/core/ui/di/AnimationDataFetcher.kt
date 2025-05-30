package com.osg.core.ui.di

fun interface AnimationDataFetcher {
    suspend fun fetchAnimationByPath(path: String): ByteArray
}