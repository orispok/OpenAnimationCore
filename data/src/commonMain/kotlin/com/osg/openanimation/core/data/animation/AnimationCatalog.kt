package com.osg.openanimation.core.data.animation

import kotlinx.serialization.Serializable

@Serializable
data class AnimationCatalog(
    val animations: Map<String, AnimationMetadata> = emptyMap(),
)