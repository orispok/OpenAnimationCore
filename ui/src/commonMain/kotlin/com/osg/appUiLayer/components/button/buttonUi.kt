package com.osg.appUiLayer.components.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import com.osg.appUiLayer.di.data.SelectedQueryType
import com.osg.appUiLayer.graph.NavigationUiItem
import com.osg.appUiLayer.util.icons.Explore
import com.osg.appUiLayer.util.icons.TrendingUp
import openanimation.appuilayer.generated.resources.Res
import openanimation.appuilayer.generated.resources.explore
import openanimation.appuilayer.generated.resources.liked
import openanimation.appuilayer.generated.resources.trending

val SelectedQueryType.ExploreCategory.buttonUi: NavigationUiItem
    get() = when (this) {
        SelectedQueryType.ExploreCategory.Trending -> NavigationUiItem(
            iconOutline = Icons.AutoMirrored.Filled.TrendingUp,
            iconFilled = Icons.AutoMirrored.Filled.TrendingUp,
            stringResource = Res.string.trending,
        )
        SelectedQueryType.ExploreCategory.Explore -> NavigationUiItem(
            iconOutline = Icons.Outlined.Explore,
            iconFilled = Icons.Filled.Explore,
            stringResource = Res.string.explore,
        )
        SelectedQueryType.ExploreCategory.Liked -> NavigationUiItem(
            iconOutline = Icons.Outlined.FavoriteBorder,
            iconFilled = Icons.Filled.Favorite,
            stringResource = Res.string.liked,
        )
    }