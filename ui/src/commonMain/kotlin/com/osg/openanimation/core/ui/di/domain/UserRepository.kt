@file:OptIn(ExperimentalCoroutinesApi::class)

package com.osg.openanimation.core.ui.di.domain

import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.data.user.UserProfile
import com.osg.openanimation.core.ui.components.signin.SignInResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


@Immutable
sealed interface UserSessionState {
    @Immutable
    data object SignedOut : UserSessionState
    @Immutable
    data class SignedIn(
        val userProfile: UserProfile,
        val favorites: Set<String>,
    ) : UserSessionState
}


@Immutable
interface UserRepository{
    val profileFlow: Flow<UserSessionState>
    suspend fun onUserDownload(hash: String)
    suspend fun likeAnimation(hash: String)
    suspend fun dislikeAnimation(hash: String)

    fun userAnimationsFlow(uid: String): Flow<List<UploadedAnimationMeta>>

    fun onUserSignOut()
    fun onRegistered(signInResultState: Result<SignInResult>)

    suspend fun updateProfile(userProfile: UserProfile)
}