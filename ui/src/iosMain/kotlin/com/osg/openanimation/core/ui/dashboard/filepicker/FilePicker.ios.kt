package com.osg.openanimation.core.ui.dashboard.filepicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIViewController
import platform.UIKit.UIWindowScene
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.UTTypeContent
import platform.darwin.NSObject
import kotlin.coroutines.resume

@Composable
actual fun FilePicker(
    show: Boolean,
    fileExtensions: List<String>,
    onFileSelected: (String?) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isPickerActive by remember { mutableStateOf(false) }

    LaunchedEffect(show) {
        if (show && !isPickerActive) {
            isPickerActive = true

            scope.launch {
                try {
                    val selectedPath = withContext(Dispatchers.Main) {
                        openDocumentPicker(fileExtensions)
                    }
                    onFileSelected(selectedPath)
                } catch (e: Exception) {
                    onFileSelected(null)
                } finally {
                    isPickerActive = false
                }
            }
        }
    }

    // Reset picker state when show becomes false
    LaunchedEffect(show) {
        if (!show) {
            isPickerActive = false
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private suspend fun openDocumentPicker(fileExtensions: List<String>): String? =
    suspendCancellableCoroutine { continuation ->

        // Create delegate to handle picker results
        val delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(
                controller: UIDocumentPickerViewController,
                didPickDocumentsAtURLs: List<*>
            ) {
                val urls = didPickDocumentsAtURLs.filterIsInstance<NSURL>()
                val selectedPath = urls.firstOrNull()?.path
                continuation.resume(selectedPath)
            }

            override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
                continuation.resume(null)
            }
        }

        // Determine content types based on file extensions
        val contentTypes = if (fileExtensions.isEmpty()) {
            listOf(UTTypeContent)
        } else {
            fileExtensions.mapNotNull { extension ->
                UTType.typeWithFilenameExtension(extension)
            }.ifEmpty { listOf(UTTypeContent) }
        }

        // Create the document picker
        val pickerController = UIDocumentPickerViewController(
            forOpeningContentTypes = contentTypes
        )

        // Configure picker
        pickerController.allowsMultipleSelection = false
        pickerController.delegate = delegate

        // Present the picker
        val topViewController = UIApplication.sharedApplication.topMostViewController()
        topViewController?.presentViewController(
            pickerController,
            animated = true,
            completion = null
        )

        // Handle cancellation
        continuation.invokeOnCancellation {
            topViewController?.dismissViewControllerAnimated(true, null)
        }
    }

// Extension to get the top-most view controller
private fun UIApplication.topMostViewController(): UIViewController? {
    val keyWindow = this.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
        ?.keyWindow

    var topController = keyWindow?.rootViewController
    while (topController?.presentedViewController != null) {
        topController = topController.presentedViewController
    }

    return topController
}