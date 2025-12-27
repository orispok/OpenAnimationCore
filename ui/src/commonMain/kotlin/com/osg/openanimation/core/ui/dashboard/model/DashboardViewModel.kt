package com.osg.openanimation.core.ui.dashboard.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.dashboard.state.DashboardUiState
import com.osg.openanimation.core.ui.dashboard.state.UploadedAnimationUiData
import com.osg.openanimation.core.ui.di.domain.AnimationUploader
import com.osg.openanimation.core.ui.di.domain.UserRepository
import com.osg.openanimation.core.ui.di.domain.UserSessionState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided


@KoinViewModel
class DashboardViewModel(
    @Provided private val animationStorage: AnimationUploader,
    @Provided private val userRepository: UserRepository,
) : ViewModel() {

    private fun signedInFlow(uid: String): Flow<DashboardUiState> =
        userRepository.userAnimationsFlow(uid)
            .map { uploadedAnimations ->
                if (uploadedAnimations.isEmpty()) {
                    DashboardUiState.Empty
                } else {
                    DashboardUiState.UploadedCollection(
                        animations = uploadedAnimations.map(::toUiData).toImmutableList(),
                    )
                }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DashboardUiState> = userRepository.profileFlow.flatMapLatest {
        when (it) {
            is UserSessionState.SignedIn -> signedInFlow(it.userProfile.uid)
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
                animationStorage.fetchUserAnimation(meta.path)
            }
        ),
        metadata = meta
    )
}