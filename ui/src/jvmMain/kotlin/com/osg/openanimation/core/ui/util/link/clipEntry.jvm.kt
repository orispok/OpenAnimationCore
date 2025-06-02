package com.osg.openanimation.core.ui.util.link

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import java.awt.datatransfer.StringSelection

@OptIn(ExperimentalComposeUiApi::class)
actual fun createClipEntryWithPlainText(text: String): ClipEntry {
    return ClipEntry(StringSelection(text))
}
