package com.osg.openanimation.core.utils

import com.osg.openanimation.core.data.animation.AnimationMetadata

fun Collection<AnimationMetadata>.extractSortedTags(): List<String> {
    val tagFrequency = mutableMapOf<String, Int>()
    forEach { metadata ->
        metadata.tags.forEach { tag ->
            tagFrequency[tag] = (tagFrequency[tag] ?: 0) + 1
        }
    }
    return tagFrequency.keys.sortedByDescending { tagFrequency[it] }
}