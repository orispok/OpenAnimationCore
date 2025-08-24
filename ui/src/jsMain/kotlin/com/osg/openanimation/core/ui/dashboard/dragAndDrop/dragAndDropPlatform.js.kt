package com.osg.openanimation.core.ui.dashboard.dragAndDrop

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.domDataTransferOrNull
import org.w3c.files.FileReader


@OptIn(ExperimentalComposeUiApi::class)
actual fun resolveDroppedContent(event: DragAndDropEvent, onReadText: (String?) -> Unit) {
    val file = event.transferData?.domDataTransferOrNull?.files?.item(0)
    file?.let {
        val reader = FileReader()
        reader.onload = {
            onReadText(reader.result as? String)
        }
        reader.onerror = {
            onReadText(null)
        }
    }
}