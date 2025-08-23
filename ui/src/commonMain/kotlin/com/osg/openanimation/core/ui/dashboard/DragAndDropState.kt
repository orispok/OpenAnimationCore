package com.osg.openanimation.core.ui.dashboard

sealed class DragAndDropState {
    data object Initial : DragAndDropState()
    data class FileDropped(val content: String) : DragAndDropState()
    data object Processing : DragAndDropState()
}

