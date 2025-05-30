package com.osg.openanimation.core.ui.di

fun interface AnimationContentLoader {
    suspend fun fetchAnimationByPath(path: String): ByteArray
}