package com.osg.openanimation.core.ui.di.domain

import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.data.stats.AnimationStats
import com.osg.openanimation.core.ui.graph.GuestQueryType
import kotlinx.coroutines.flow.Flow

interface AnimationMetadataRepository {
    suspend fun fetchMetaByHash(hash: String): AnimationMetadata
    suspend fun fetchRelatedAnimations(
        animationMetadata: AnimationMetadata,
        count: Int = 4
    ): List<AnimationMetadata>
    fun animationStatsFlow(hash: String): Flow<AnimationStats>
    suspend fun fetchBy(queryType: GuestQueryType, limit: Int): List<AnimationMetadata>
    suspend fun fetchTags(): List<String>
}