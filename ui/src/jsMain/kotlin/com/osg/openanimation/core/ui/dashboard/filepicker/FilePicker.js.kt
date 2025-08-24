package com.osg.openanimation.core.ui.dashboard.filepicker

import androidx.compose.runtime.Composable

@Composable
actual fun FilePicker(
    show: Boolean,
    fileExtensions: List<String>,
    onFileSelected: (String?) -> Unit
) {
}