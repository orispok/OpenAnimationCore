package com.osg.openanimation.core.ui.file

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual val fileService: FileService
    get() = FileServiceImpl

object FileServiceImpl : FileService, KoinComponent {
    override fun saveFile(byteArray: ByteArray, fileName: String) {
        val jsonData = byteArray.decodeToString()
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, jsonData)

            // (Optional) Here you're setting the title of the content
            putExtra(Intent.EXTRA_TITLE, "lottie animation")
            type = "text/json"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(get(), shareIntent, null)
    }
}