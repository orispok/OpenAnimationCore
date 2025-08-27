package com.osg.openanimation.core.ui.dashboard.dragAndDrop

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import java.net.URI
import kotlin.io.path.readText
import kotlin.io.path.toPath

@OptIn(ExperimentalComposeUiApi::class)
actual fun resolveDroppedContent(event: DragAndDropEvent, onReadText: (String?) -> Unit) {
    val dragData = event.dragData() as? DragData.FilesList
    val filePath = dragData?.readFiles()?.first()
    if (filePath != null) {
        val fileContent = URI.create(filePath).toPath().readText()
        onReadText(fileContent)
    } else {
        onReadText(null)
    }
}