package com.osg.openanimation.core.ui.color

import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch

data class ProcessedJson(
    val data: String,
    val transformOptionsIdx: Int,
)

data class OptionsWithColors(
    val options: List<List<Color>>,
    val nodes: List<StaticColorNode>
)


class ColorsEditHandler(
    private val animationContentLoader: AnimationContentLoader,
    private val path: String,
    private val scope: CoroutineScope,
){
    private val transformOptionsList = List(12){
        TransformOptions(hueShift = (it * 30).toFloat())
    }

    private val expandedState = MutableStateFlow(false)

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
//        val grouped = colorNodes.associateBy {
//            Color(
//                red = it.color.red.roundTo(2),
//                green = it.color.green.roundTo(2),
//                blue = it.color.blue.roundTo(2),
//                alpha = it.color.alpha.roundTo(2)
//            )
//        }

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
        expandedState,
        selectedOptionsState,
        updatedJsonState
    ) { optionsWithColors, expanded, idx, updatedJson ->
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
            expanded = expanded,
            processedJsonState = processedJsonState
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = ColorsEditPalette()
    )

    fun onSelectColorTransformOption(index: Int){
        scope.launch {
            if(index == 0) {
                expandedState.value = !expandedState.value
            }else{
                selectedOptionsState.value = index
                expandedState.value = false
            }
        }
    }
}