package com.osg.appUiLayer.di

import org.osg.previewopenanimation.core.data.report.ReportSubmission

interface SubmitReportHandler {
    suspend fun submit(reportSubmission: ReportSubmission)
}