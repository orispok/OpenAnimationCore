@file:OptIn(ExperimentalCoroutinesApi::class)

package com.osg.openanimation.core.ui.di

import com.osg.openanimation.core.data.use.UserProfile
import com.osg.openanimation.core.ui.components.signin.SignInResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


sealed interface UserSessionState {
    data object SignedOut : UserSessionState
    data class SignedIn(
        val userProfile: UserProfile,
        val favorites: Set<String>,
    ) : UserSessionState
}


interface UserRepository{
    val profileFlow: Flow<UserSessionState>
    suspend fun onUserDownload(hash: String)
    suspend fun likeAnimation(hash: String)
    suspend fun dislikeAnimation(hash: String)
    fun onUserSignOut()
    fun onRegistered(signInResultState: Result<SignInResult>)
}