package com.osg.openanimation.core.ui.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.document
import org.khronos.webgl.Uint8Array
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.File

fun ByteArray.toBitsArray(): Array<Uint8Array> {
    return arrayOf(Uint8Array(this.toTypedArray()))
}

class ExportServiceJsImpl : ExportService {
    @Composable
    override fun ExportFile(dataString: String, fileName: String, onFinished: () -> Unit) {
        // Create a blob
        val file = File(
            fileBits = dataString.encodeToByteArray().toBitsArray(),
            fileName = fileName,
        )

        val a = document.createElement("a") as HTMLAnchorElement
        a.href = URL.createObjectURL(file)
        a.download = fileName

        LaunchedEffect(Unit) {
            a.click()
            onFinished()
        }
    }
}

actual val exportService: ExportService by lazy {
    ExportServiceJsImpl()
}