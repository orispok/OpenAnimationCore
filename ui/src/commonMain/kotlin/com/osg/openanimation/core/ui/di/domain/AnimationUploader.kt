package com.osg.openanimation.core.ui.di.domain

import com.osg.openanimation.core.data.upload.ModerationStatus
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import kotlinx.coroutines.flow.Flow

interface AnimationUploader {
    suspend fun processUploadAnimation(animationContent: String): String
    suspend fun uploadAnimation(
        uploadedAnimationMeta: UploadedAnimationMeta,
        animationContent: String? = null
    )

    fun moderationStatusFlow(hash: String): Flow<ModerationStatus>
    fun uploadedMetaFlow(hash: String): Flow<UploadedAnimationMeta>

    suspend fun onRemoveAnimation(hash: String)

    suspend fun fetchUserAnimation(path: String): String
}