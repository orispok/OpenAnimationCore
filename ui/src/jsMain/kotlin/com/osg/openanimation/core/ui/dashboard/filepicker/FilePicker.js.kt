package com.osg.openanimation.core.ui.dashboard.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.files.FileReader

@Composable
actual fun FilePicker(
    show: Boolean,
    fileExtensions: List<String>,
    onFileSelected: (String?) -> Unit
) {
    if (!show) return

    // Create a file input element
    val input = document.createElement("input") as HTMLInputElement
    input.type = "file"
    input.accept = ".json"
    input.style.display = "none"
    input.multiple = false

    // Add the input to the document
    document.body?.appendChild(input)

    // Setup the change listener
    input.onchange = { event ->
        try {
            // Get the selected files
            val file = event.target
                ?.unsafeCast<HTMLInputElement>()
                ?.files
                ?.asList()?.first()
            if (file != null) {
                val reader = FileReader()
                reader.onload = {
                    onFileSelected(reader.result as? String)
                }
                reader.onerror = {
                    onFileSelected(null)
                }

            }

        } catch (e: Throwable) {
            println("Error reading file: ${e.message}")
        } finally {
            document.body?.removeChild(input)
        }
    }

    input.oncancel = {
        document.body?.removeChild(input)
    }

    LaunchedEffect(show) {
        input.click()
    }
}