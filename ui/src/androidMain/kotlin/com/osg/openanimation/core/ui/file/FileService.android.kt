package com.osg.openanimation.core.ui.file

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

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
        var isStarted by remember { mutableStateOf(false) }
        if (isStarted){
            startActivity(koinInject(), shareIntent, null)
        }
        LaunchedEffect(Unit) {
            isStarted = true
            onFinished()
        }
    }
}