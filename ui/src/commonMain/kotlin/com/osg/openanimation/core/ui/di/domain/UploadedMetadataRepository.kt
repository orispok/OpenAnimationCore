package com.osg.openanimation.core.ui.di.domain

import com.osg.openanimation.core.data.upload.ModerationStatus
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import kotlinx.coroutines.flow.Flow

interface UploadedMetadataRepository {
    fun moderationStatusFlow(hash: String): Flow<ModerationStatus>
    fun uploadedMetaFlow(hash: String): Flow<UploadedAnimationMeta>

    suspend fun onRemoveAnimation(hash: String)
}