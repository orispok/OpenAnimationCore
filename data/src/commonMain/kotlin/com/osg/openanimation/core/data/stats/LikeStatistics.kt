package com.osg.openanimation.core.data.stats

import kotlinx.serialization.Serializable

@Serializable
data class LikeRecord(
    val timestamp: Long,
)

@Serializable
data class LikeStatistics(
    val animationsRecords: Map<String, LikeRecord>,
)