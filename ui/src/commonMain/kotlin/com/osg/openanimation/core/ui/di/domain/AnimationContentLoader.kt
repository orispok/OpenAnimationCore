package com.osg.openanimation.core.ui.di.domain

fun interface AnimationContentLoader {
    suspend fun fetchAnimationByPath(path: String): String
}

interface StorageService {
    suspend fun uploadAnimationContent(path: String, content: ByteArray)
    suspend fun removeAnimationContent(path: String)
    suspend fun fetchUserAnimation(path: String): String
}
