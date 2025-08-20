package com.osg.openanimation.core.ui.details.model.ds

import com.osg.openanimation.core.data.stats.AnimationStats
import com.osg.openanimation.core.ui.details.DialogType
import com.osg.openanimation.core.ui.home.domain.AnimationUiData
import com.osg.openanimation.core.ui.home.domain.ColorPaletteWithMetadata

data class AnimationAndRelated(
    val animationUiData: ColorPaletteWithMetadata,
    val relatedAnimations: List<AnimationUiData>,
)

data class DetailsUiPane(
    val animationUiData: ColorPaletteWithMetadata,
    val animationStats: AnimationStats,
    val dialogToShow: DialogType? = null,
    val isDownloadTransition: Boolean = false,
    val isLiked: Boolean = false,
)

data class LikeStatsData(
    val animationStats: AnimationStats,
    val isLiked: Boolean,
)

sealed interface DetailsScreenStates {
    data object Loading : DetailsScreenStates
    data class Success(
        val detailsUiPane: DetailsUiPane,
        val relatedAnimations: List<AnimationUiData>,
    ) : DetailsScreenStates
}

data class ButtonTransitionState(
    val isDownloadTransition: Boolean = false,
)