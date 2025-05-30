package com.osg.appUiLayer.di

import com.osg.appUiLayer.di.data.GuestQueryType
import kotlinx.coroutines.flow.Flow
import org.osg.previewopenanimation.core.data.animation.AnimationMetadata
import org.osg.previewopenanimation.core.data.stats.AnimationStats

interface AnimationMetadataRepository {
    suspend fun fetchMetaByHash(hash: String): AnimationMetadata
    suspend fun fetchTradingAnimationIds(): Set<String>
    suspend fun fetchRelatedAnimations(
        animationMetadata: AnimationMetadata,
        count: Int = 4
    ): List<AnimationMetadata>
    fun animationStatsFlow(hash: String): Flow<AnimationStats>
    suspend fun fetchBy(queryType: GuestQueryType, limit: Int): List<AnimationMetadata>
    suspend fun fetchTags(): List<String>
}