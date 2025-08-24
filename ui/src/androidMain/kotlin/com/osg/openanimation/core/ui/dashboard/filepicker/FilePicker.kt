package com.osg.openanimation.core.ui.dashboard.filepicker

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import java.io.InputStreamReader

@Composable
actual fun FilePicker(
    show: Boolean,
    fileExtensions: List<String>,
    onFileSelected: (String?) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val contentResolver = context.contentResolver
            contentResolver.openInputStream(it)?.use { inputStream ->
                val reader = InputStreamReader(inputStream)
                val content = reader.readText()
                onFileSelected(content)
            }
        } ?: onFileSelected(null)
    }

    if (show) {
        LaunchedEffect(Unit) {
            launcher.launch("*/*")
        }
    }
}
