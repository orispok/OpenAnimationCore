package com.osg.openanimation.core.ui.dashboard

sealed interface DragAndDropState {
    data object Initial : DragAndDropState
    data object Processing : DragAndDropState
}

