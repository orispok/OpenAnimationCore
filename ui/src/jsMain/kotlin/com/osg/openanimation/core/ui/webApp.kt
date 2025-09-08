package com.osg.openanimation.core.ui

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import com.osg.openanimation.core.ui.preload.PreLoadFallback
import fadeOutElement
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalBrowserHistoryApi::class, ExperimentalComposeUiApi::class)
fun webApp(
    baseApp: BaseApp
) {
    onWasmReady {
        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
            PreLoadFallback{
                fadeOutElement()
                baseApp.AppEntry {
                    it.bindToBrowserNavigation()
                }
            }
        }
    }
}
