package com.osg.openanimation.core.data.upload

import kotlinx.serialization.Serializable

@Serializable
data class UploadedAnimation(
    val animationId: String,
    val uploadTimestamp: Long,
    val moderationStatus: ModerationStatus = ModerationStatus.PENDING,
    val title: String,
    val description: String,
    val tags: List<String> = emptyList(),
)