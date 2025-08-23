package com.osg.openanimation.core.data.upload

import kotlinx.serialization.Serializable

@Serializable
enum class ModerationStatus {
    PENDING,        // Awaiting moderation
    APPROVED,       // Approved by moderator
    REJECTED,       // Rejected by moderator
    AUTO_APPROVED   // Auto-approved based on user reputation/criteria
}
