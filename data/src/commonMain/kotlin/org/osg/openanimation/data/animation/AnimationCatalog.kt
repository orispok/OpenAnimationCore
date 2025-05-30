package org.osg.previewopenanimation.core.data.animation

import kotlinx.serialization.Serializable

@Serializable
data class AnimationCatalog(
    val animations: Map<String, AnimationMetadata> = emptyMap(),
)