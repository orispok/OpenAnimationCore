package com.osg.openanimation.core.ui.file

import androidx.compose.runtime.Composable

interface ExportService{
    @Composable
    fun ExportFile(dataString :String, fileName: String, onFinished: () -> Unit)
}

expect val exportService: ExportService