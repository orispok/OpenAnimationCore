package com.osg.openanimation.core.ui.home.model

import com.osg.core.di.data.FilterQueryType
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import com.osg.core.di.data.SelectedQueryType
import com.osg.openanimation.core.ui.home.domain.AnimationUiData
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
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

fun Collection<AnimationMetadata>.filterSortByText(
    filterChipType: FilterQueryType
): List<AnimationMetadata> {
    return when (filterChipType) {
        is SelectedQueryType.Tag -> {
            filter { it.tags.contains(filterChipType.keySearch) }.sortedBy {
                it.tags.indexOf(filterChipType.keySearch)
            }
        }
        is SelectedQueryType.FreeText -> {
            filter { it.name.contains(filterChipType.keySearch, ignoreCase = true) ||
                    it.tags.any { tag ->
                        tag.contains(filterChipType.keySearch, ignoreCase = true)
                    }
            }.sortedWith(
                freeTextComparator(filterChipType.keySearch)
            )
        }
    }
}

fun freeTextComparator(
    freeText: String
) = compareBy<AnimationMetadata> {
    it.name == freeText
}.thenBy {
    it.tags.any { tag ->
        tag.contains(freeText, ignoreCase = true)
    }
}

fun List<AnimationMetadata>.toUiDataList(
    animationRepository: AnimationContentLoader
): List<AnimationUiData> {
    if (isEmpty()) return emptyList()
    return map {
        AnimationUiData(
            animationState = AnimationDataState(
                hash = it.hash,
            ) {
                animationRepository.fetchAnimationByPath(it.localFileName)
            },
            metadata = it,
        )
    }
}