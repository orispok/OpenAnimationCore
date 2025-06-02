package com.osg.openanimation.core.ui.home.model

import com.osg.openanimation.core.ui.di.data.FilterQueryType
import com.osg.openanimation.core.ui.di.data.SelectedQueryType
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import com.osg.openanimation.core.ui.home.domain.AnimationUiData

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