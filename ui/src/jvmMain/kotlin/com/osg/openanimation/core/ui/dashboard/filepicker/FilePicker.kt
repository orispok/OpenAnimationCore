package com.osg.openanimation.core.ui.dashboard.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog
import java.io.File
import java.io.FilenameFilter

@Composable
actual fun FilePicker(
    show: Boolean,
    fileExtensions: List<String>,
    onFileSelected: (String?) -> Unit
) {
    LaunchedEffect(show) {
        if (show) {
            val window = ComposeWindow()
            val fileDialog = FileDialog(window, "Select Lo JSON File", FileDialog.LOAD)
            fileDialog.filenameFilter = FilenameFilter { _: File, name: String ->
                fileExtensions.contains(name.lowercase().substringAfterLast("."))
            }

            // Show the dialog and handle the result
            fileDialog.isVisible = true

            val selectedFile = fileDialog.files.firstOrNull()
            if (selectedFile != null) {
                try {
                    val content = selectedFile.readText()
                    onFileSelected(content)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onFileSelected(null)
                }
            } else {
                onFileSelected(null)
            }
            window.dispose()
        }
    }

}
