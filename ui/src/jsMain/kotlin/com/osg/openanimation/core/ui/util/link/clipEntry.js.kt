package com.osg.openanimation.core.ui.util.link

import androidx.compose.ui.platform.ClipEntry

actual fun createClipEntryWithPlainText(text: String): ClipEntry {
    return ClipEntry.withPlainText(text)
}