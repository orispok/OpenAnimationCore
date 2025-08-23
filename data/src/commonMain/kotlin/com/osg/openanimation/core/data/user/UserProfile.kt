package com.osg.openanimation.core.data.user

import com.osg.openanimation.core.data.use.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val uid: String,
    val firstName: String,
    val email: String? = null,
    val role: UserRole = UserRole.USER,
)