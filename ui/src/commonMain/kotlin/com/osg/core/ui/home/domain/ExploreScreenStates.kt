package com.osg.core.ui.home.domain

import com.osg.core.di.data.SelectedQueryType

sealed interface ExploreScreenStates{
    data class GridData(
        val animations: List<AnimationUiData>,
        val selected: SelectedQueryType = SelectedQueryType.ExploreCategory.Explore,
        val categories: List<SelectedQueryType.Tag>
    ): ExploreScreenStates
    data object RequiredLogin: ExploreScreenStates
    data object Loading: ExploreScreenStates
}