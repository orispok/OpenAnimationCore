package com.osg.openanimation.core.ui.home.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.ui.di.data.GuestQueryType
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import com.osg.openanimation.core.ui.di.data.SelectedQueryType
import com.osg.openanimation.core.ui.di.AnimationMetadataRepository
import com.osg.openanimation.core.ui.home.domain.ExploreScreenStates
import com.osg.openanimation.core.ui.di.UserSessionState
import com.osg.openanimation.core.ui.di.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AnimationViewModel(

    initialQueryType: SelectedQueryType = SelectedQueryType.ExploreCategory.Explore
) : ViewModel(), KoinComponent {
    private val animationMetaRepo: AnimationMetadataRepository by inject()
    private val animationContentLoader: AnimationContentLoader by inject()
    private val userRepository: UserRepository by inject()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ExploreScreenStates> = userRepository.profileFlow.mapLatest {
        resolveUiState(
            userSessionState = it,
            queryType = initialQueryType
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000),
        initialValue = ExploreScreenStates.Loading
    )



    internal suspend fun resolveUiState(
        userSessionState: UserSessionState,
        queryType: SelectedQueryType,
    ): ExploreScreenStates {
        return when (queryType) {
            is GuestQueryType -> {
                val animations = animationMetaRepo.fetchBy(queryType, Int.MAX_VALUE).toUiDataList(animationContentLoader)
                return ExploreScreenStates.Success(
                    animations = animations,
                    selected = queryType,
                    categories = animationMetaRepo.fetchTags().map {
                        SelectedQueryType.Tag(it)
                    }
                )
            }
            is SelectedQueryType.ExploreCategory.Liked -> {
                if (userSessionState is UserSessionState.SignedIn) {
                    val animations = userSessionState.favorites
                        .map { animationMetaRepo.fetchMetaByHash(it) }
                        .toUiDataList(animationContentLoader)
                    return ExploreScreenStates.Success(
                        animations = animations,
                        selected = queryType,
                        categories = animationMetaRepo.fetchTags().map {
                            SelectedQueryType.Tag(it)
                        }
                    )
                } else {
                    ExploreScreenStates.RequiredLogin
                }
            }
        }
    }
}


