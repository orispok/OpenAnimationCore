package com.osg.openanimation.core.ui.dashboard.dragAndDrop

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.domDataTransferOrNull
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@OptIn(ExperimentalComposeUiApi::class)
actual suspend fun resolveDroppedContent(event: DragAndDropEvent): String? {
    val file = event.transferData?.domDataTransferOrNull?.files?.item(0)
    return file?.let {
        val reader = FileReader()
        suspendCoroutine { continuation ->
            reader.onload = {
                continuation.resume(reader.result as? String)
            }
            reader.onerror = {
                continuation.resume(null)
            }
            reader.readAsText(file)
        }
    }
}