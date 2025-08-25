package com.osg.openanimation.core.ui.di.domain

import com.osg.openanimation.core.data.upload.ModerationStatus
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta

interface UploadedMetadataRepository {
    suspend fun fetchAnimationModerationStatus(hash: String): ModerationStatus
    suspend fun fetchAnimationUploadedMeta(hash: String): UploadedAnimationMeta

    suspend fun onRemoveAnimation(hash: String)
}