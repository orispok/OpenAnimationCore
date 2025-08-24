package com.osg.openanimation.core.ui.dashboard.dragAndDrop

import androidx.compose.ui.draganddrop.DragAndDropEvent

expect suspend fun resolveDroppedContent(
    event: DragAndDropEvent,
): String?