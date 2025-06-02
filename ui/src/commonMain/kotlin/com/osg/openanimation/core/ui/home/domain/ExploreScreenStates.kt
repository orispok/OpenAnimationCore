package com.osg.openanimation.core.ui.home.domain

import com.osg.openanimation.core.ui.di.data.SelectedQueryType

sealed interface ExploreScreenStates{
    data class Success(
        val animations: List<AnimationUiData>,
        val selected: SelectedQueryType = SelectedQueryType.ExploreCategory.Explore,
        val categories: List<SelectedQueryType.Tag>
    ): ExploreScreenStates
    data object RequiredLogin: ExploreScreenStates
    data object Loading: ExploreScreenStates
}