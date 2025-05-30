package org.osg.previewopenanimation.core.data.report

import kotlinx.serialization.Serializable

enum class ReportReasonOptions{
    INAPPROPRIATE,
    COPYRIGHT,
    SPAM,
    EXPLICIT_CONTENT,
    FALSE_INFORMATION,
    OTHER
}

@Serializable
data class ReportSubmission(
    val reason: ReportReasonOptions,
    val moreInfo: String,
    val animationHash: String,
)