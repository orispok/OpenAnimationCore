@file:OptIn(ExperimentalCoroutinesApi::class)

package com.osg.appUiLayer.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.osg.previewopenanimation.core.data.use.UserProfile


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