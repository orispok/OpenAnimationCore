package com.osg.openanimation.core.ui.dashboard.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.files.File
import org.w3c.files.FileReader

@Composable
actual fun FilePicker(
    show: Boolean,
    fileExtensions: List<String>,
    onFileSelected: (String?) -> Unit
) {
    if (!show) return


    LaunchedEffect(show) {
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
                    readBytes(file){ bytes ->
                        // Convert bytes to string
                        val content = bytes.decodeToString()
                        // Pass the content
                        onFileSelected(content)
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

        // Trigger the file picker
        input.click()
    }
}

fun readBytes(
    file: File,
    onRead: (ByteArray) -> Unit
){
    val reader = FileReader()
    reader.onload = { event ->
        try {
            // Read the file as an ArrayBuffer
            val arrayBuffer = event
                .target
                ?.unsafeCast<FileReader>()
                ?.result
                ?.unsafeCast<ArrayBuffer>()
                ?: throw IllegalStateException("Could not read file")

            // Convert the ArrayBuffer to a ByteArray
            val bytes = Uint8Array(arrayBuffer)

            // Copy the bytes into a ByteArray
            val byteArray = ByteArray(bytes.length)
            for (i in 0 until bytes.length) {
                byteArray[i] = bytes[i]
            }

            // Return the ByteArray
            onRead(byteArray)
        } catch (e: Exception) {
            println("Error reading file: ${e.message}")
        }
    }

    // Read the file as an ArrayBuffer
    reader.readAsArrayBuffer(file)
}