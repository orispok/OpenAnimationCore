package com.osg.openanimation.core.ui.file

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.koinInject

actual val exportService: ExportService
    get() = ExportServiceImpl

object ExportServiceImpl : ExportService {
    @Composable
    override fun ExportFile(dataString: String, fileName: String, onFinished: () -> Unit){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, dataString)
            // (Optional) Here you're setting the title of the content
            putExtra(Intent.EXTRA_TITLE, "lottie animation")
            type = "text/json"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        val context = koinInject<Context>()
        context.startActivity(shareIntent, null)
        LaunchedEffect(Unit) {
            onFinished()
        }
    }
}