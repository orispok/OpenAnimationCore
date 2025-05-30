package com.osg.core.ui.di

import com.osg.openanimation.core.data.report.ReportSubmission


fun interface ReportSubmissionService {
    suspend fun submit(reportSubmission: ReportSubmission)
}