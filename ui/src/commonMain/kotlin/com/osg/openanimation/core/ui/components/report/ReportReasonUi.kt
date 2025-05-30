package com.osg.openanimation.core.ui.components.report

import com.osg.openanimation.core.data.report.ReportReasonOptions

val ReportReasonOptions.displayName: String
    get() = when (this) {
        ReportReasonOptions.INAPPROPRIATE -> "Inappropriate Content"
        ReportReasonOptions.COPYRIGHT -> "Copyright Violation"
        ReportReasonOptions.SPAM -> "Spam or Scam"
        ReportReasonOptions.EXPLICIT_CONTENT -> "Explicit Content"
        ReportReasonOptions.FALSE_INFORMATION -> "False Information"
        ReportReasonOptions.OTHER -> "Other"
    }