package com.osg.openanimation.core.ui.dashboard.dragAndDrop

import androidx.compose.ui.draganddrop.DragAndDropEvent

expect fun resolveDroppedText(
    event: DragAndDropEvent,
): String?