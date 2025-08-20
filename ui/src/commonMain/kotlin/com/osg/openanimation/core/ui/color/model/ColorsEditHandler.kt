package com.osg.openanimation.core.ui.color.model

import androidx.compose.ui.graphics.Color
import com.osg.openanimation.core.ui.color.util.getKMostDifferentColors
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

data class ProcessedJson(
    val data: String,
    val transformOptionsIdx: Int,
)

data class OptionsWithColors(
    val options: List<List<Color>>,
    val nodes: List<PaintNode>
)


class ColorsEditHandler(
    private val animationContentLoader: AnimationContentLoader,
    private val path: String,
    private val scope: CoroutineScope,
){
    private val transformOptionsList by lazy {
        val hueValues = listOf(
            0f, 30f, 90f
        )
        val chromaValues = listOf(
            0f, 30f, 90f
        )
        buildList {
            for (hue in hueValues) {
                for (chroma in chromaValues) {
                    add(TransformOptions(hueShift = hue, chromaShift = chroma))
                }
            }
        }
    }


    private val selectedTransformOptionIndexState = MutableStateFlow(0)

    private val sourceAnimationDataFlow = flow {
        emit(animationContentLoader.fetchAnimationByPath(path))
    }.shareIn(
        scope = scope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        replay = 1
    )


    private val transformedAnimationJsonState = combine(
        sourceAnimationDataFlow,
        selectedTransformOptionIndexState
    ) { animationData, selectedTransformOptionIndex ->
        if (selectedTransformOptionIndex == 0) {
            ProcessedJson(
                data = animationData,
                transformOptionsIdx = 0
            )
        }else{
            ProcessedJson(
                data = updateColorsInJson(
                    animationData,
                    extractColorsFromJson(animationData),
                    transformOptionsList[selectedTransformOptionIndex]
                ),
                transformOptionsIdx = selectedTransformOptionIndex
            )
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        replay = 1
    )

    private val transformOptions = sourceAnimationDataFlow.map { colorNodes ->
        val colorNodes = extractColorsFromJson(colorNodes)
        val colors = colorNodes.map { it.colors }.flatten().getKMostDifferentColors(5)
        OptionsWithColors(
            options = transformOptionsList.map { option ->
                option.transform(colors)
            },
            nodes = colorNodes
        )
    }

    suspend fun getProcessedJson(): String {
        return transformedAnimationJsonState.first().data
    }

    val uiState = combine(
        transformOptions,
        selectedTransformOptionIndexState,
        transformedAnimationJsonState
    ) { availableColorTransformations, selectedTransformOptionIndex, currentTransformedJson ->
        val processedJsonState = if (selectedTransformOptionIndex == currentTransformedJson.transformOptionsIdx) {
            AnimationDataState.LazyLoading(
                hash = path,
                lazyLoader = {
                    currentTransformedJson.data
                }
            )
        } else {
            AnimationDataState.Processing
        }
        ColorsEditPalette(
            options = availableColorTransformations.options,
            processedJsonState = processedJsonState,
            selectedOptionIndex = selectedTransformOptionIndex,
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = ColorsEditPalette()
    )

    fun onSelectColorTransformOption(selectedOptionIndex: Int){
        selectedTransformOptionIndexState.value = selectedOptionIndex
    }
}