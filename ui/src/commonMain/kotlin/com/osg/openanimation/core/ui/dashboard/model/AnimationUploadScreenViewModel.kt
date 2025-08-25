package com.osg.openanimation.core.ui.dashboard.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osg.openanimation.core.data.upload.ModerationStatus
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.ui.color.model.ColorsEditHandler
import com.osg.openanimation.core.ui.color.model.ColorsEditPalette
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.dashboard.AnimationUploadUiState
import com.osg.openanimation.core.ui.di.domain.AnimationContentLoader
import com.osg.openanimation.core.ui.di.domain.AnimationUploader
import com.osg.openanimation.core.ui.di.domain.UploadedMetadataRepository
import com.osg.openanimation.core.ui.graph.EditAnimation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Provided

private data class ModerationMeta(
    val moderation: ModerationStatus,
    val uploadedAnimationMeta: UploadedAnimationMeta,
)

@KoinViewModel
class AnimationUploadScreenViewModel(
    @InjectedParam private val arg: EditAnimation,
    @Provided private val dataFetcher: AnimationContentLoader,
    @Provided private val metaFetcher: UploadedMetadataRepository,
    @Provided private val animationUploader: AnimationUploader
) : ViewModel() {

    private val animationNameStateFlow = MutableStateFlow<String?>(null)

    private val tagsStateFlow = MutableStateFlow<Set<String>?>(null)

    private val animationMetadataFlow = metaFetcher
        .uploadedMetaFlow(arg.animationId)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            replay = 1
        )


    private val moderationDetailsFlow: Flow<ModerationMeta> = combine(
        animationMetadataFlow,
        animationNameStateFlow,
        tagsStateFlow,
        metaFetcher.moderationStatusFlow(arg.animationId),
    ) { animationMeta, name, tags, mod ->
        val updatedMeta = animationMeta.copy(
            name = name ?: animationMeta.name,
            tags = tags?.toList() ?: animationMeta.tags,
        )

        ModerationMeta(
            moderation = mod,
            uploadedAnimationMeta = updatedMeta
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val animationColorsEditHandlerFlow: SharedFlow<ColorsEditHandler> =
        animationMetadataFlow.map {
            val animationJson = dataFetcher.fetchAnimationByPath(it.path)
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
    private val animationColorsEditPaletteFlow: Flow<ColorsEditPalette> =
        animationColorsEditHandlerFlow.flatMapConcat { handler ->
            handler.uiState
        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            replay = 1
        )


    val uiState: StateFlow<AnimationUploadUiState> = combine(
        moderationDetailsFlow,
        animationColorsEditPaletteFlow,
        animationNameStateFlow,
        tagsStateFlow,
    ) { moderationMeta, animationData, title, tags ->
        AnimationUploadUiState.Ready(
            editableAnimation = animationData,
            uploadedAnimationMeta = moderationMeta.uploadedAnimationMeta,
            moderationStatus = moderationMeta.moderation,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AnimationUploadUiState.Loading
    )


    fun onPaletteSelected(paletteIdx: Int) {
        viewModelScope.launch {
            val handler = animationColorsEditHandlerFlow.first()
            handler.onSelectColorTransformOption(paletteIdx)
        }
    }




    fun onTitleChanged(title: String) {
        animationNameStateFlow.value = title
    }

    fun onTagsChanged(tags: Set<String>) {
        tagsStateFlow.value = tags
    }


    private fun commitAnimation(
        isPublish: Boolean
    ){
        viewModelScope.launch {
            val currentMeta = animationMetadataFlow.first()
            val currentName = animationNameStateFlow.value ?: currentMeta.name
            val currentTags = tagsStateFlow.value ?: currentMeta.tags.toSet()
            val editPalette = animationColorsEditPaletteFlow.first()

            val processedJson = if (editPalette.selectedOptionIndex >= 0) {
                editPalette.processedJsonState as AnimationDataState.LazyLoading
                editPalette.processedJsonState.lazyLoader()
            } else {
                null
            }

            val updatedMeta = currentMeta.copy(
                name = currentName,
                tags = currentTags.toList(),
            )

            animationUploader.uploadAnimation(
                uploadedAnimationMeta = if(isPublish) updatedMeta.copy(
                    isSubmitted = true
                ) else updatedMeta,
                animationContent = processedJson,
            )
        }
    }
    fun onPublishClick() {
        commitAnimation(true)
    }

    fun onSaveClick() {
        commitAnimation(false)
    }

    fun onRemovalClick(
        onRemoved: () -> Unit
    ) {
        viewModelScope.launch {
            val currentMeta = animationMetadataFlow.first()
            metaFetcher.onRemoveAnimation(currentMeta.hash)
            onRemoved()
        }
    }

}