package com.osg.openanimation.core.ui.components.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import com.osg.core.di.data.SelectedQueryType
import com.osg.openanimation.core.ui.graph.NavigationUiItem
import com.osg.openanimation.core.ui.util.icons.Explore
import com.osg.openanimation.core.ui.util.icons.TrendingUp
import openanimationapp.core.ui.generated.resources.Res
import openanimationapp.core.ui.generated.resources.explore
import openanimationapp.core.ui.generated.resources.liked
import openanimationapp.core.ui.generated.resources.trending


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