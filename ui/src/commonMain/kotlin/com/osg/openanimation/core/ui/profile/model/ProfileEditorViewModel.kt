package com.osg.openanimation.core.ui.profile.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.ui.di.domain.UserRepository
import com.osg.openanimation.core.ui.di.domain.UserSessionState
import com.osg.openanimation.core.ui.profile.state.ProfileScreenState
import com.osg.openanimation.core.ui.profile.state.UserProfileUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
class ProfileEditorViewModel(
    @Provided private val userRepository: UserRepository
): ViewModel() {
    private val userSessionStateFlow: StateFlow<UserSessionState> =
        userRepository.profileFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UserSessionState.SignedOut
        )

    val uiState: StateFlow<ProfileScreenState> = userSessionStateFlow.map { session ->
        when (session) {
            is UserSessionState.SignedOut -> ProfileScreenState.SignedOut
            is UserSessionState.SignedIn -> {
                val profile = session.userProfile
                ProfileScreenState.Success(
                    UserProfileUi(
                        displayName = profile.displayName,
                        role = profile.role,
                        bio = profile.bio,
                        githubUrl = profile.githubUrl,
                        linkedinUrl = profile.linkedinUrl,
                        imageBitmap = null // TODO: integrate imageBitmap with a real ImageLoader implementation
                    )
                )
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        ProfileScreenState.Loading
    )

    fun saveProfile(
        userProfileUi: UserProfileUi
    ) {
        val session = userSessionStateFlow.value
        if (session is UserSessionState.SignedIn) {
            val profile = session.userProfile.copy(
                displayName = userProfileUi.displayName,
                bio = userProfileUi.bio,
                githubUrl = userProfileUi.githubUrl,
                linkedinUrl = userProfileUi.linkedinUrl,
            )

            viewModelScope.launch {
                userRepository.updateProfile(profile)
            }
        }
    }
}