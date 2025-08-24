package com.osg.openanimation.core.ui.dashboard.dragAndDrop

import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.toAndroidDragEvent


actual suspend fun resolveDroppedContent(event: DragAndDropEvent): String? {
    return event.toAndroidDragEvent().clipData?.getItemAt(0)?.text?.toString()
}