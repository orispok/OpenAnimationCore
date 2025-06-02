package com.osg.openanimation.core.ui.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import kotlin.coroutines.resume

class ExportServiceJvm : ExportService {
    @Composable
    override fun ExportFile(dataString: String, fileName: String, onFinished: () -> Unit) {

        LaunchedEffect(dataString) {
            val selectedFile = saveFile(
                suggestedName = fileName,
                extension = "json", // Assuming text file for simplicity
            )
            withContext(Dispatchers.IO) {
                    selectedFile?.let { file ->
                        try {
                            // Write the data to the file
                            file.writeText(dataString)

                            // Call onFinished on the main thread
                            withContext(Dispatchers.Main) {
                                onFinished()
                            }
                        }catch (e: Exception) {
                            e.printStackTrace()
                            // Call onFinished even if there was an error
                            withContext(Dispatchers.Main) {
                                onFinished()
                            }
                        }
                    }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun saveFile(
        suggestedName: String,
        extension: String?,
        frame: Frame? = null,
    ): File? = suspendCancellableCoroutine { continuation ->
        fun handleResult(value: Boolean, files: Array<File>?) {
            if (value) {
                val file = files?.firstOrNull()
                continuation.resume(file)
            }
        }

        val dialog = object : FileDialog(frame, "Save dialog", SAVE) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                handleResult(value, files)
            }
        }

        // Set file name
        dialog.file = when {
            extension != null -> "$suggestedName.$extension"
            else -> suggestedName
        }

        // Show the dialog
        dialog.isVisible = true

        // Dispose the dialog when the continuation is cancelled
        continuation.invokeOnCancellation { dialog.dispose() }
    }
}

actual val exportService: ExportService = ExportServiceJvm()
