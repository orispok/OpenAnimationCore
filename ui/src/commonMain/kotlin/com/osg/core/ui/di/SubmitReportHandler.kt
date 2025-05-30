package com.osg.core.ui.di

import com.osg.openanimation.core.data.report.ReportSubmission


fun interface SubmitReportHandler {
    suspend fun submit(reportSubmission: ReportSubmission)
}