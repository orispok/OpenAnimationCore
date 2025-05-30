@file:OptIn(ExperimentalCoroutinesApi::class)

package com.osg.core.ui.di

import com.osg.openanimation.core.data.use.UserProfile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


sealed interface UserProfileStates {
    data object SignedOut : UserProfileStates
    data class SignedIn(
        val userProfile: UserProfile,
        val favorites: Set<String>,
    ) : UserProfileStates
}


interface UserRepository{
    val profileFlow: Flow<UserProfileStates>
    suspend fun onUserDownload(hash: String)
    suspend fun likeAnimation(hash: String)
    suspend fun dislikeAnimation(hash: String)
    fun onUserSignOut()
}