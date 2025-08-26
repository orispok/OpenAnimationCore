package com.osg.openanimation.core.ui.dashboard.dragAndDrop

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.domDataTransferOrNull
import com.osg.openanimation.core.ui.dashboard.filepicker.readBytes


@OptIn(ExperimentalComposeUiApi::class)
actual fun resolveDroppedContent(event: DragAndDropEvent, onReadText: (String?) -> Unit) {
    val file = event.transferData?.domDataTransferOrNull?.files?.item(0)
    file?.let {
        readBytes(file){ bytes ->
            // Convert bytes to string
            val content = bytes.decodeToString()
            // Pass the content
            onReadText(content)
        }
    }
}