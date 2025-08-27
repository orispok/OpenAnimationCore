package com.osg.openanimation.core.ui.di.domain

fun interface AnimationContentLoader {
    suspend fun fetchAnimationByPath(path: String): String
}