package com.osg.openanimation.core.ui.dashboard.model

import androidx.lifecycle.ViewModel
import com.osg.openanimation.core.ui.dashboard.AnimationUploadUiState
import kotlinx.coroutines.flow.StateFlow

class AnimationUploadScreenViewModel: ViewModel() {
    val uiState: StateFlow<AnimationUploadUiState>
        get() = TODO()

    fun onPaletteSelected(paletteIdx: Int) {
        TODO()
    }

    fun onTitleChanged(filePath: String?) {
        TODO()
    }

    fun onUploadClick() {
        TODO()
    }

    fun onTagsChanged(tags: Set<String>) {
        TODO()
    }

}