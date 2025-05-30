package com.osg.openanimation.core.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import com.osg.openanimation.core.ui.di.AnimationMetadataRepository
import com.osg.openanimation.core.ui.file.fileService
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
    val signInState: UserSessionState,
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
    private val metaState : Flow<AnimationUiData>  = flow {
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
    }

    val uiState : StateFlow<DetailsScreenStates> = combine(
        metaState,
        userRepository.profileFlow,
        metaFetcher.animationStatsFlow(animationHash),
    ) { meta, loggedState, stats ->
        val details = DetailsUiPane(
            animationState = meta.animationState,
            metadata = meta.metadata,
            signInState = loggedState,
            animationStats = stats,
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

    fun onLikeClick(isLiked: Boolean) {
        viewModelScope.launch {
            if (isLiked) {
                userRepository.likeAnimation(animationHash)
            }else{
                userRepository.dislikeAnimation(animationHash)
            }
        }
    }

    fun onDownloadClick(downloadRequest: AnimationMetadata) {
        viewModelScope.launch {
            val animationState = dataFetcher.fetchAnimationByPath(downloadRequest.localFileName)
            val saveName = downloadRequest.localFileName.substringAfterLast('/')
            fileService.saveFile(
                byteArray = animationState,
                fileName = saveName,
            )
            userRepository.onUserDownload(animationHash)
        }
    }

}