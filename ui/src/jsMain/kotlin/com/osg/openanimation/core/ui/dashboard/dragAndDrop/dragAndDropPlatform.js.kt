package com.osg.openanimation.core.ui.dashboard.dragAndDrop

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.domDataTransferOrNull


@OptIn(ExperimentalComposeUiApi::class)
actual fun resolveDroppedText(event: DragAndDropEvent): String? {
    return event.transferData?.domDataTransferOrNull?.getData(
        "text/plain"
    )
}