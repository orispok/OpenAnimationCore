package com.osg.core.ui

import com.osg.core.ui.preload.PreLoadFallback
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToNavigation
import fadeOutElement
import kotlinx.browser.window
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
                    window.bindToNavigation(it)
                }
            }
        }
    }
}
