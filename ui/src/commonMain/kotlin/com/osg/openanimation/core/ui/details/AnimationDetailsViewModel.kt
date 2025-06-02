package com.osg.openanimation.core.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.data.stats.AnimationStats
import com.osg.openanimation.core.ui.components.lottie.toAnimationDataState
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import com.osg.openanimation.core.ui.di.AnimationMetadataRepository
import com.osg.openanimation.core.ui.di.UserRepository
import com.osg.openanimation.core.ui.di.UserSessionState
import com.osg.openanimation.core.ui.home.domain.AnimationUiData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class AnimationAndRelated(
    val animationUiData: AnimationUiData,
    val relatedAnimations: List<AnimationUiData>,
)

data class DetailsUiPane(
    val animationUiData: AnimationUiData,
    val animationStats: AnimationStats,
    val dialogToShow: DialogType? = null,
    val isDownloadTransition: Boolean = false,
    val isLiked: Boolean = false,
)

data class LikeStatsData(
    val animationStats: AnimationStats,
    val isLiked: Boolean,
)

sealed interface DetailsScreenStates {
    data object Loading : DetailsScreenStates
    data class Success(
        val detailsUiPane: DetailsUiPane,
        val relatedAnimations: List<AnimationUiData>,
    ) : DetailsScreenStates
}

data class ButtonTransitionState(
    val isDownloadTransition: Boolean = false,
)

class AnimationDetailsViewModel(
    private val animationHash: String,
): ViewModel(), KoinComponent {
    private val dataFetcher: AnimationContentLoader by inject()
    private val metaFetcher: AnimationMetadataRepository by inject()
    private val userRepository: UserRepository by inject()
    private val userActionRequestState = MutableStateFlow<DialogType?>(null)
    private val buttonTransitionState = MutableStateFlow(ButtonTransitionState())


    private val animationUiState : SharedFlow<AnimationAndRelated>  = flow {
        val meta = metaFetcher.fetchMetaByHash(animationHash)
        val relatedAnimations =  metaFetcher.fetchRelatedAnimations(
            animationMetadata = meta,
            count = 4
        )
        emit(
            AnimationAndRelated(
                animationUiData = dataFetcher.toAnimationDataState(meta),
                relatedAnimations = relatedAnimations.map(dataFetcher::toAnimationDataState)
            )
        )
    }.shareIn(
        replay = 1,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
    )

    private val userSharedFlow: StateFlow<UserSessionState> = userRepository.profileFlow
        .stateIn(
            initialValue = UserSessionState.SignedOut,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
        )

    private val statsLikeFlow = combine(
        metaFetcher.animationStatsFlow(animationHash),
        userRepository.profileFlow
    ){ stats, userSessionState ->
        val isLiked = when (userSessionState) {
            is UserSessionState.SignedIn -> userSessionState.favorites.contains(animationHash)
            UserSessionState.SignedOut -> false
        }
        LikeStatsData(
            animationStats = stats,
            isLiked = isLiked
        )
    }

    val uiState : StateFlow<DetailsScreenStates> = combine(
        animationUiState,
        userSharedFlow,
        statsLikeFlow,
        userActionRequestState,
        buttonTransitionState,
    ) { animationAndRelated, loggedState, statsLike, dialogToShow, buttonTransitions->
        val details = DetailsUiPane(
            animationUiData = animationAndRelated.animationUiData,
            animationStats = statsLike.animationStats,
            dialogToShow = dialogToShow,
            isLiked = statsLike.isLiked,
            isDownloadTransition = buttonTransitions.isDownloadTransition
        )

        DetailsScreenStates.Success(
            detailsUiPane = details,
            relatedAnimations = animationAndRelated.relatedAnimations
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DetailsScreenStates.Loading
    )

    fun onLikeClick(nextLike: Boolean): Boolean {
        val currentUserState = userSharedFlow.value
        return when(currentUserState){
            is UserSessionState.SignedIn -> {
                reactOnAnimation(nextLike = nextLike)
                return nextLike
            }
            UserSessionState.SignedOut -> {
                userActionRequestState.value = DialogType.SignInDialog
                false
            }
        }
    }

    fun onDownloadClick(downloadRequest: AnimationMetadata) {
        buttonTransitionState.value = buttonTransitionState.value.copy(
            isDownloadTransition = true
        )
        viewModelScope.launch {
            val currentUserState = userSharedFlow.first()
            when(currentUserState){
                is UserSessionState.SignedIn -> {
                    val animationMeta = animationUiState.first().animationUiData
                    userActionRequestState.value = DialogType.Export.Success(
                        fileName = downloadRequest.localFileName.substringAfterLast('/'),
                        animationData = dataFetcher
                            .fetchAnimationByPath(animationMeta.metadata.localFileName)
                            .decodeToString()
                    )
                }
                UserSessionState.SignedOut -> {
                    userActionRequestState.value = DialogType.SignInDialog
                }
            }
            buttonTransitionState.value = buttonTransitionState.value.copy(
                isDownloadTransition = false
            )
        }

    }

    private fun reactOnAnimation(
        nextLike: Boolean,
    ){
        viewModelScope.launch {
            if (nextLike) {
                userRepository.likeAnimation(animationHash)
            }else{
                userRepository.dislikeAnimation(animationHash)
            }
        }
    }

    fun onDismissSignInDialog() {
        userActionRequestState.value = null
    }
}