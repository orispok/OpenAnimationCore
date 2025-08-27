package com.osg.openanimation.core.ui.di.domain

import com.osg.openanimation.core.data.report.ReportSubmission


fun interface ReportSubmissionService {
    suspend fun submit(reportSubmission: ReportSubmission)
}