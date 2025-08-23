package com.osg.openanimation.core.data.use

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    USER,           // Regular user
    MODERATOR,      // Can moderate uploads
    ADMIN           // Full admin privileges
}