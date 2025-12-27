package com.osg.openanimation.core.data.upload

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
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