package com.osg.openanimation.core.ui.graph

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.osg.openanimation.core.ui.di.data.SelectedQueryType
import com.osg.openanimation.core.ui.util.serialilze.decodeFromHexString
import com.osg.openanimation.core.ui.util.serialilze.toHexString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable @SerialName("Destination")
sealed interface Destination{
    @Serializable @SerialName("Home")
    data class Home(
        val searchQuerySerial: String,
    ) : Destination {
        constructor(selectedQueryType: SelectedQueryType = SelectedQueryType.ExploreCategory.Trending) : this(
            selectedQueryType.toHexString()
        )
        fun resolveQuery(): SelectedQueryType {
            return searchQuerySerial.decodeFromHexString()
        }
    }


    @Serializable @SerialName("AnimationDetails")
    data class AnimationDetails(
        val hash: String,
    ): Destination

}


val NavBackStackEntry?.currentGraph : Destination?
    get(){
        val destination = this?.destination?: return null
        return if (destination.hasRoute(Destination.Home::class)){
             toRoute(Destination.Home::class)
        }else if (destination.hasRoute(Destination.AnimationDetails::class)) {
            toRoute(Destination.AnimationDetails::class)
        }
        else null
    }
