package com.osg.openanimation.core.ui.home.domain

import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.ui.graph.SelectedQueryType
import kotlinx.collections.immutable.ImmutableList

sealed interface ExploreScreenStates{
    @Immutable
    data class Success(
        val animations: ImmutableList<AnimationUiData>,
        val selected: SelectedQueryType = SelectedQueryType.ExploreCategory.Explore,
        val categories: ImmutableList<SelectedQueryType.Tag>
    ): ExploreScreenStates
    data object RequiredLogin: ExploreScreenStates
    data object Loading: ExploreScreenStates
}