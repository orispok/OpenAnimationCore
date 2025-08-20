package com.osg.openanimation.core.ui.color.model

import androidx.compose.ui.graphics.Color
import com.osg.openanimation.core.ui.color.util.getKMostDifferentColors
import com.osg.openanimation.core.ui.color.util.toHsl
import com.osg.openanimation.core.ui.components.lottie.AnimationDataState
import com.osg.openanimation.core.ui.di.AnimationContentLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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
    private val transformOptionsList = List(12){
        TransformOptions(
            hueShift = (it * 30).toFloat(),
            lightnessShift = if (it % 4 == 0) 0.1f else 0f
        )
    }


    private val selectedOptionsState = MutableStateFlow(0)

    private val animationDataFlow = flow {
        emit(animationContentLoader.fetchAnimationByPath(path))
    }.shareIn(
        scope = scope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        replay = 1
    )


    private val updatedJsonState = combine(
        animationDataFlow,
        selectedOptionsState
    ) { animationData, selectedOptionIndex ->
        if (selectedOptionIndex == 0) {
            ProcessedJson(
                data = animationData,
                transformOptionsIdx = 0
            )
        }else{
            ProcessedJson(
                data = updateColorsInJson(
                    animationData,
                    extractColorsFromJson(animationData),
                    transformOptionsList[selectedOptionIndex]
                ),
                transformOptionsIdx = selectedOptionIndex
            )
        }
    }

    private val transformOptions = animationDataFlow.map { colorNodes ->
        val colorNodes = extractColorsFromJson(colorNodes)
        val colors = colorNodes.map { it.color }.flatten().getKMostDifferentColors(5)
        OptionsWithColors(
            options = transformOptionsList.map { option ->
                option.transform(colors).sortedBy {
                    it.toHsl().hue
                }
            },
            nodes = colorNodes
        )
    }

    val uiState = combine(
        transformOptions,
        selectedOptionsState,
        updatedJsonState
    ) { optionsWithColors, idx, updatedJson ->
        val processedJsonState = if (idx == updatedJson.transformOptionsIdx) {
            AnimationDataState.LazyLoading(
                hash = path,
                lazyLoader = {
                    updatedJson.data
                }
            )
        } else {
            AnimationDataState.Processing
        }
        ColorsEditPalette(
            options = optionsWithColors.options,
            processedJsonState = processedJsonState,
            selectedOptionIndex = idx,
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = ColorsEditPalette()
    )

    fun onSelectColorTransformOption(index: Int){
        selectedOptionsState.value = index
    }
}