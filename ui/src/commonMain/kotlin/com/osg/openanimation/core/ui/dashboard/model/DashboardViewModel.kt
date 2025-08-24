package com.osg.openanimation.core.ui.dashboard.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.dashboard.state.DashboardUiState
import com.osg.openanimation.core.ui.dashboard.state.UploadedAnimationUiData
import com.osg.openanimation.core.ui.di.domain.AnimationContentLoader
import com.osg.openanimation.core.ui.di.domain.UserRepository
import com.osg.openanimation.core.ui.di.domain.UserSessionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class DashboardViewModel(
    private val dataFetcher: AnimationContentLoader,
    private val userRepository: UserRepository,
): ViewModel() {

    private val signedInFlow: Flow<DashboardUiState> = userRepository.userAnimationsFlow().map {uploadedAnimations ->
        if (uploadedAnimations.isEmpty()) {
            DashboardUiState.Empty
        } else {
            DashboardUiState.UploadedCollection(
                animations = uploadedAnimations.map(::toUiData),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DashboardUiState> = userRepository.profileFlow.flatMapLatest{
        when(it) {
            is UserSessionState.SignedIn -> signedInFlow
            UserSessionState.SignedOut -> flowOf(DashboardUiState.SignedOut)
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState.Loading
    )



    private fun toUiData(meta: UploadedAnimationMeta) = UploadedAnimationUiData(
        animationState = AnimationDataState.LazyLoading(
            hash = meta.path,
            lazyLoader = {
                dataFetcher.fetchAnimationByPath(meta.path)
            }
        ),
        metadata = meta
    )
}