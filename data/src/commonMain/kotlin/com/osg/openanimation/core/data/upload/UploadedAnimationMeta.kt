package com.osg.openanimation.core.data.upload

import kotlinx.serialization.Serializable

@Serializable
data class UploadedAnimationMeta(
    val animationId: String,
    val uploadTimestamp: Long,
    val moderationStatus: ModerationStatus = ModerationStatus.PENDING,
    val name: String,
    val description: String,
    val path: String,
    val tags: List<String> = emptyList(),
)