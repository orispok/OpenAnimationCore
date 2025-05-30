package com.osg.core.ui.util.link

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.toClipEntry

actual fun createClipEntryWithPlainText(text: String): ClipEntry {
    return ClipData.newPlainText("", text).toClipEntry()
}