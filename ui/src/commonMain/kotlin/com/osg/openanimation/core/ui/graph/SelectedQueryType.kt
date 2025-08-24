package com.osg.openanimation.core.ui.graph

import com.osg.openanimation.core.ui.graph.SelectedQueryType.ExploreCategory.Explore
import com.osg.openanimation.core.ui.graph.SelectedQueryType.ExploreCategory.Liked
import com.osg.openanimation.core.ui.graph.SelectedQueryType.ExploreCategory.Trending
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SelectedQueryType {
    val keySearch: String
        get() = ""


    @Serializable
    sealed interface ExploreCategory : SelectedQueryType, RailDestination {
        @Serializable @SerialName("Explore")
        data object Explore : ExploreCategory, GuestQueryType

        @Serializable @SerialName("Trending")
        data object Trending : ExploreCategory, GuestQueryType

        @Serializable @SerialName("Liked")
        data object Liked : ExploreCategory
    }

    @Serializable @SerialName("Tag")
    data class Tag(val tag: String) : FilterQueryType{
        override val keySearch: String = tag
    }
    @Serializable @SerialName("FreeText")
    data class FreeText(val text: String) : FilterQueryType{
        override val keySearch: String = text
    }

}


sealed interface RailDestination{
    companion object{
        val entries: List<RailDestination> = listOf(
            Dashboard,
            Trending,
            Explore,
            Liked,
        )
    }
}

@Serializable
sealed interface GuestQueryType: SelectedQueryType

@Serializable
sealed interface FilterQueryType: GuestQueryType


@Serializable @SerialName("Dashboard")
data object Dashboard: RailDestination, Destination

@Serializable @SerialName("EditAnimation")
data class EditAnimation(
    val animationId: String,
) : Destination