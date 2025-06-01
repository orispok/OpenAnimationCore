package com.osg.openanimation.core.ui.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSItemProvider
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UIKit.UIActivityViewController

object ExportServiceImpl : ExportService {
    @OptIn(BetaInteropApi::class)
    @Composable
    override fun ExportFile(dataString: String, fileName: String, onFinished: () -> Unit){
        val controller = LocalUIViewController.current
        val nsString = NSString.create(dataString)
        val itemProvider = NSItemProvider(item =  nsString, typeIdentifier = "public.json")
        val activityViewController = UIActivityViewController(listOf(itemProvider), null)
        controller.presentViewController(activityViewController, animated = true, completion = null)
        LaunchedEffect(Unit){
            onFinished()
        }
    }
}

actual val exportService: ExportService
    get() = ExportServiceImpl