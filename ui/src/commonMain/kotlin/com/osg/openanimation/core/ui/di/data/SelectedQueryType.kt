package com.osg.openanimation.core.ui.di.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
sealed interface SelectedQueryType {
    val keySearch: String
        get() = ""

    @Serializable
    sealed interface ExploreCategory : SelectedQueryType {
        @Serializable @SerialName("Explore")
        data object Explore : ExploreCategory, GuestQueryType

        @Serializable @SerialName("Trending")
        data object Trending : ExploreCategory, GuestQueryType

        @Serializable @SerialName("Liked")
        data object Liked : ExploreCategory


        companion object{
            val entries: List<ExploreCategory> = listOf(
                Trending,
                Explore,
                Liked,
            )
        }
    }

    @Serializable
    data class Tag(val tag: String) : FilterQueryType{
        override val keySearch: String = tag
    }
    @Serializable
    data class FreeText(val text: String) : FilterQueryType{
        override val keySearch: String = text
    }
}

@Serializable
sealed interface GuestQueryType: SelectedQueryType

@Serializable
sealed interface FilterQueryType: GuestQueryType