package com.osg.openanimation.core.ui.di.domain

fun interface AnimationInitialUpload {
    suspend fun processUploadAnimation(animationContent: String): String
}