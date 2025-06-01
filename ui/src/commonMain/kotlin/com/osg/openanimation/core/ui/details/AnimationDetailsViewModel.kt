package com.osg.openanimation.core.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import com.osg.openanimation.core.ui.di.AnimationMetadataRepository
import com.osg.openanimation.core.ui.home.domain.AnimationUiData
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.di.UserSessionState
import com.osg.openanimation.core.ui.di.UserRepository
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.data.stats.AnimationStats
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class DetailsUiPane(
    val animationState: AnimationDataState,
    val metadata: AnimationMetadata,
    val animationStats: AnimationStats,
    val dialogToShow: DialogType? = null,
    val isLiked: Boolean = false,
)
sealed interface DetailsScreenStates {
    data object Loading : DetailsScreenStates
    data class Success(
        val detailsUiPane: DetailsUiPane,
        val relatedAnimations: List<AnimationUiData>,
    ) : DetailsScreenStates
}


class AnimationDetailsViewModel(
    private val animationHash: String,
): ViewModel(), KoinComponent {
    private val dataFetcher: AnimationContentLoader by inject()
    private val metaFetcher: AnimationMetadataRepository by inject()
    private val userRepository: UserRepository by inject()
    private val userActionRequestState = MutableStateFlow<DialogType?>(null)

    private val animationUiState : SharedFlow<AnimationUiData>  = flow {
        val animationMeta = metaFetcher.fetchMetaByHash(animationHash)
        val animationUi =  AnimationUiData(
            animationState = AnimationDataState(
                hash = animationMeta.hash,
            ){
                dataFetcher.fetchAnimationByPath(animationMeta.localFileName)
            },
            metadata = animationMeta,
        )
        emit(animationUi)
    }.shareIn(
        replay = 1,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
    )

    private val userSharedFlow: SharedFlow<UserSessionState> = userRepository.profileFlow
        .shareIn(
            replay = 1,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
        )

    val uiState : StateFlow<DetailsScreenStates> = combine(
        animationUiState,
        userSharedFlow,
        metaFetcher.animationStatsFlow(animationHash),
        userActionRequestState
    ) { meta, loggedState, stats, dialogToShow ->
        val details = DetailsUiPane(
            animationState = meta.animationState,
            metadata = meta.metadata,
            animationStats = stats,
            dialogToShow = dialogToShow,
            isLiked = when (loggedState) {
                is UserSessionState.SignedIn -> loggedState.favorites.contains(meta.metadata.hash)
                UserSessionState.SignedOut -> false
            }
        )
        val relatedAnimations =  metaFetcher.fetchRelatedAnimations(
            animationMetadata = meta.metadata,
            count = 4
        ).map { animationMeta ->
            AnimationUiData(
                animationState = AnimationDataState(
                    hash = animationMeta.hash,
                ){
                    dataFetcher.fetchAnimationByPath(animationMeta.localFileName)
                },
                metadata = animationMeta,
            )
        }
        DetailsScreenStates.Success(
            detailsUiPane = details,
            relatedAnimations = relatedAnimations,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DetailsScreenStates.Loading
    )

    fun onLikeClick(isCurrentLike: Boolean) {
        userActionRequestState.value = DialogType.Export.Loading
        viewModelScope.launch {
            val currentUserState = userSharedFlow.first()
            when(currentUserState){
                is UserSessionState.SignedIn -> {
                    reactOnAnimation(currentIsLike = isCurrentLike)
                }
                UserSessionState.SignedOut -> {
                    userActionRequestState.value = DialogType.SignInDialog
                }
            }
        }
    }

    fun onDownloadClick(downloadRequest: AnimationMetadata) {
        userActionRequestState.value = DialogType.Export.Loading
        viewModelScope.launch {
            val currentUserState = userSharedFlow.first()
            when(currentUserState){
                is UserSessionState.SignedIn -> {
                    val animationMeta = animationUiState.first()

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
        }
    }

    private fun reactOnAnimation(
        currentIsLike: Boolean,
    ){
        viewModelScope.launch {
            if (currentIsLike) {
                userRepository.dislikeAnimation(animationHash)
            }else{
                userRepository.likeAnimation(animationHash)
            }
        }
    }

    fun onDismissSignInDialog() {
        userActionRequestState.value = null
    }
}