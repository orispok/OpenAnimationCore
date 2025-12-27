package com.osg.openanimation.core.ui.profile.state

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import com.osg.openanimation.core.data.use.UserRole

@Immutable
data class UserProfileUi(
    val displayName: String,
    val bio: String? = null,
    val imageBitmap: ImageBitmap? = null,
    val linkedinUrl: String? = null,
    val githubUrl: String? = null,
    val role: UserRole = UserRole.USER
)

sealed interface ProfileScreenState {
    data object Loading : ProfileScreenState
    data object SignedOut : ProfileScreenState
    @Immutable
    data class Success(val userProfileUi: UserProfileUi) : ProfileScreenState
}