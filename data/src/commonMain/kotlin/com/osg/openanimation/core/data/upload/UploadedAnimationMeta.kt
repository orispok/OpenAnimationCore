package com.osg.openanimation.core.data.upload

import kotlinx.serialization.Serializable

@Serializable
data class UploadedAnimationMeta(
    val hash: String,
    val uploadTimestamp: Long,
    val name: String,
    val description: String,
    val path: String,
    val isSubmitted: Boolean,
    val tags: List<String> = emptyList(),
)