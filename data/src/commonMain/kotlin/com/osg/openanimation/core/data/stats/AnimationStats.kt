package com.osg.openanimation.core.data.stats

import kotlinx.serialization.Serializable

@Serializable
data class AnimationStats(
    val downloadCount: Int = 0,
    val likeCount: Int = 0,
)