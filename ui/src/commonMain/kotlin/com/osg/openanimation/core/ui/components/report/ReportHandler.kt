package com.osg.openanimation.core.ui.components.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.data.report.ReportReasonOptions
import com.osg.openanimation.core.data.report.ReportSubmission
import com.osg.openanimation.core.ui.di.domain.ReportSubmissionService
import com.osg.openanimation.core.ui.di.domain.UserRepository
import com.osg.openanimation.core.ui.di.domain.UserSessionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

enum class FailedReason{
    NetworkError,
    NotSignedIn,
}

sealed interface ReportUiState {
    data object Initial : ReportUiState
    sealed interface SubmitProcess: ReportUiState{
        data object Sending : SubmitProcess
        data object Sent : SubmitProcess

        data class Failed(
            val reason: FailedReason
        ) : SubmitProcess
    }
}

data class ReportSubmissionUi(
    val reason: ReportReasonOptions,
    val moreInfo: String,
    val animationHash: String,
)

@KoinViewModel
class ReportViewModel(
    @Provided private val reportHandler: ReportSubmissionService,
    @Provided private val userRepository: UserRepository,
): ViewModel() {
    val uiState = MutableStateFlow<ReportUiState>(ReportUiState.Initial)
    fun submitReport(reportSubmission: ReportSubmissionUi){
        uiState.value = ReportUiState.SubmitProcess.Sending
        viewModelScope.launch {
            val s = userRepository.profileFlow.first()
            uiState.value = when(s){
                is UserSessionState.SignedIn -> {
                    try {
                        reportHandler.submit(
                            ReportSubmission(
                                reason = reportSubmission.reason,
                                moreInfo = reportSubmission.moreInfo,
                                animationHash = reportSubmission.animationHash,
                                uid = s.userProfile.uid,
                            )
                        )
                        ReportUiState.SubmitProcess.Sent
                    }catch (e: Exception){
                        ReportUiState.SubmitProcess.Failed(
                            reason = FailedReason.NetworkError
                        )
                    }
                }
                UserSessionState.SignedOut -> {
                    ReportUiState.SubmitProcess.Failed(
                        reason = FailedReason.NotSignedIn
                    )

                }
            }
        }
    }
}