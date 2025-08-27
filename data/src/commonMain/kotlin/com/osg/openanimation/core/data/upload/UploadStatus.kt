package com.osg.openanimation.core.data.upload

import kotlinx.serialization.Serializable

@Serializable
enum class ModerationStatus {
    DRAFT,
    PENDING,        // Awaiting moderation
    APPROVED,       // Approved by moderator
    REJECTED,       // Rejected by moderator
}
