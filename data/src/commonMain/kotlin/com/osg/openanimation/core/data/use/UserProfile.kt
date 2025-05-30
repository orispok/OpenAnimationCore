package com.osg.openanimation.core.data.use

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val uid: String,
    val firstName: String,
    val email: String? = null,
)