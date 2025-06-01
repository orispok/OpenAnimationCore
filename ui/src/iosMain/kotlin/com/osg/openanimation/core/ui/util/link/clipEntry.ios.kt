package com.osg.openanimation.core.ui.util.link

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

@OptIn(ExperimentalComposeUiApi::class)
actual fun createClipEntryWithPlainText(text: String): ClipEntry {
    // Assuming ClipEntry has a constructor that takes plain text
    return ClipEntry.withPlainText(text)
}
