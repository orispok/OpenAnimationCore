package com.osg.appUiLayer.components.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.appUiLayer.di.SubmitReportHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.osg.previewopenanimation.core.data.report.ReportSubmission


sealed interface ReportUiState {
    data object Initial : ReportUiState
    sealed interface SubmitProcess: ReportUiState{
        data object Sending : SubmitProcess
        data object Sent : SubmitProcess
    }
}

class ReportViewModel: ViewModel(), KoinComponent {
    val reportHandler by inject<SubmitReportHandler>()
    val uiState = MutableStateFlow<ReportUiState>(ReportUiState.Initial)
    fun submitReport(reportSubmission: ReportSubmission){
        uiState.value = ReportUiState.SubmitProcess.Sending
        viewModelScope.launch {
            reportHandler.submit(reportSubmission)
            uiState.value = ReportUiState.SubmitProcess.Sent
        }
    }
}