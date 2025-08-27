package com.osg.openanimation.core.data.user

import com.osg.openanimation.core.data.use.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val uid: String,
    val displayName: String,
    val photoUrl: String? = null,
    val role: UserRole = UserRole.USER,
    val bio: String? = null,
    val email: String? = null,
    val linkedinUrl: String? = null,
    val githubUrl: String? = null,
)