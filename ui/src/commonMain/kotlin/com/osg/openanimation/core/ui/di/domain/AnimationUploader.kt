package com.osg.openanimation.core.ui.di.domain

import com.osg.openanimation.core.data.upload.UploadedAnimationMeta

interface AnimationUploader {
    suspend fun processUploadAnimation(animationContent: String): String
    suspend fun uploadAnimation(
        uploadedAnimationMeta: UploadedAnimationMeta,
        animationContent: String? = null
    )
}