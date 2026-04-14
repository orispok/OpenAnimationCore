package com.osg.openanimation.core.ui

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import com.osg.openanimation.core.ui.preload.PreLoadFallback
import com.osg.openanimation.core.ui.preload.fadeOutElement

@OptIn(ExperimentalBrowserHistoryApi::class, ExperimentalComposeUiApi::class)
fun webApp() {
    ComposeViewport(
        viewportContainerId = "ComposeTarget",
        content = {
            PreLoadFallback{
                fadeOutElement()
                AppEntry {
                    it.bindToBrowserNavigation()
                }
            }
        })
}
