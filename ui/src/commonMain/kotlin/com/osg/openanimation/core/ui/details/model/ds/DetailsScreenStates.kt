package com.osg.openanimation.core.ui.details.model.ds

import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.data.stats.AnimationStats
import com.osg.openanimation.core.ui.details.DialogType
import com.osg.openanimation.core.ui.home.domain.AnimationUiData
import com.osg.openanimation.core.ui.home.domain.ColorPaletteWithMetadata
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AnimationAndRelated(
    val animationUiData: ColorPaletteWithMetadata,
    val relatedAnimations: ImmutableList<AnimationUiData>,
)

@Immutable
data class DetailsUiPane(
    val animationUiData: ColorPaletteWithMetadata,
    val animationStats: AnimationStats,
    val dialogToShow: DialogType? = null,
    val isDownloadTransition: Boolean = false,
    val isLiked: Boolean = false,
)

@Immutable
data class LikeStatsData(
    val animationStats: AnimationStats,
    val isLiked: Boolean,
)

@Immutable
sealed interface DetailsScreenStates {
    @Immutable
    data object Loading : DetailsScreenStates
    @Immutable
    data class Success(
        val detailsUiPane: DetailsUiPane,
        val relatedAnimations: ImmutableList<AnimationUiData>,
    ) : DetailsScreenStates
}

data class ButtonTransitionState(
    val isDownloadTransition: Boolean = false,
)