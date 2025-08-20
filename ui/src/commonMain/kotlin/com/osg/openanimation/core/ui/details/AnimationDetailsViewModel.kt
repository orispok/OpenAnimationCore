package com.osg.openanimation.core.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.data.stats.AnimationStats
import com.osg.openanimation.core.ui.color.model.ColorsEditHandler
import com.osg.openanimation.core.ui.color.model.ColorsEditPalette
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import com.osg.openanimation.core.ui.di.AnimationMetadataRepository
import com.osg.openanimation.core.ui.di.UserRepository
import com.osg.openanimation.core.ui.di.UserSessionState
import com.osg.openanimation.core.ui.home.domain.AnimationUiData
import com.osg.openanimation.core.ui.home.domain.ColorPaletteWithMetadata
import com.osg.openanimation.core.ui.home.model.toUiDataList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class AnimationAndRelated(
    val animationUiData: ColorPaletteWithMetadata,
    val relatedAnimations: List<AnimationUiData>,
)

data class DetailsUiPane(
    val animationUiData: ColorPaletteWithMetadata,
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
) : ViewModel(), KoinComponent {
    private val dataFetcher: AnimationContentLoader by inject()
    private val metaFetcher: AnimationMetadataRepository by inject()
    private val userRepository: UserRepository by inject()
    private val userActionRequestState = MutableStateFlow<DialogType?>(null)
    private val buttonTransitionState = MutableStateFlow(ButtonTransitionState())

    private val animationMetadataFlow = flow {
        emit(metaFetcher.fetchMetaByHash(animationHash))
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        replay = 1
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val animationColorsEditHandlerFlow: SharedFlow<ColorsEditHandler> = animationMetadataFlow.map {
        val animationJson = dataFetcher.fetchAnimationByPath(it.localFileName)
        ColorsEditHandler(
            animationContentLoader = { animationJson },
            scope = viewModelScope,
            path = it.hash,
        )
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        replay = 1
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val animationColorsEditPaletteFlow: Flow<ColorsEditPalette> = animationColorsEditHandlerFlow.flatMapConcat { handler ->
        handler.uiState
    }
    private val animationWithColorPaletteFlow = combine(
        animationMetadataFlow,
        animationColorsEditPaletteFlow
    ) { metadata, animationData ->
        ColorPaletteWithMetadata(
            metadata = metadata,
            editableAnimation = animationData
        )
    }

    private val relatedAnimationsFlow = animationMetadataFlow.map { metadata ->
        val relatedAnimations = metaFetcher.fetchRelatedAnimations(
            animationMetadata = metadata,
            count = 4
        )
        relatedAnimations.toUiDataList(
            dataFetcher
        )
    }

    private val animationAndRelatedFlow: Flow<AnimationAndRelated> = combine(
        animationWithColorPaletteFlow,
        relatedAnimationsFlow
    ) { animationWithColorPalette, relatedAnimations ->
        AnimationAndRelated(
            animationUiData = animationWithColorPalette,
            relatedAnimations = relatedAnimations
        )
    }

    private val userSessionStateFlow: StateFlow<UserSessionState> = userRepository.profileFlow
        .stateIn(
            initialValue = UserSessionState.SignedOut,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
        )

    private val animationLikeStatsFlow = combine(
        metaFetcher.animationStatsFlow(animationHash),
        userRepository.profileFlow
    ) { animationStats, userSessionState ->
        val isLiked = when (userSessionState) {
            is UserSessionState.SignedIn -> userSessionState.favorites.contains(animationHash)
            UserSessionState.SignedOut -> false
        }
        LikeStatsData(
            animationStats = animationStats,
            isLiked = isLiked
        )
    }

    val uiState: StateFlow<DetailsScreenStates> = combine(
        animationAndRelatedFlow,
        userSessionStateFlow,
        animationLikeStatsFlow,
        userActionRequestState,
        buttonTransitionState,
    ) { animationAndRelated, loggedState, statsLike, dialogToShow, buttonTransitions ->
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
        val currentUserState = userSessionStateFlow.value
        return when (currentUserState) {
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
            val currentUserState = userSessionStateFlow.first()
            when (currentUserState) {
                is UserSessionState.SignedIn -> {
                    val animationMeta = animationAndRelatedFlow.first().animationUiData
                    userActionRequestState.value = DialogType.Export.Success(
                        fileName = downloadRequest.downloadFileName(),
                        animationData = animationColorsEditHandlerFlow.first().getProcessedJson()
                    )
                    async {
                        userRepository.onUserDownload(animationMeta.metadata.hash)
                    }
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
    ) {
        viewModelScope.launch {
            if (nextLike) {
                userRepository.likeAnimation(animationHash)
            } else {
                userRepository.dislikeAnimation(animationHash)
            }
        }
    }

    fun onDismissSignInDialog() {
        userActionRequestState.value = null
    }

    fun onPalletSelect(index: Int) {
        viewModelScope.launch {
            val handler = animationColorsEditHandlerFlow.first()
            handler.onSelectColorTransformOption(index)
        }
    }
}

fun AnimationMetadata.downloadFileName(): String {
    return name.replace(" ", "_").lowercase() + ".json"
}
