package com.osg.openanimation.core.ui.util.adaptive

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

enum class ScreenSizeClass {
    COMPACT,
    MEDIUM,
    EXPANDED
}

val screenWidthClass: ScreenSizeClass
    @Composable
    get() {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        return when(windowSizeClass.windowWidthSizeClass){
            WindowWidthSizeClass.COMPACT -> ScreenSizeClass.COMPACT
            WindowWidthSizeClass.MEDIUM -> ScreenSizeClass.MEDIUM
            WindowWidthSizeClass.EXPANDED -> ScreenSizeClass.EXPANDED
            else -> ScreenSizeClass.MEDIUM
        }
    }

val screenHeightClass: ScreenSizeClass
    @Composable
    get() {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        return when(windowSizeClass.windowHeightSizeClass){
            WindowHeightSizeClass.COMPACT -> ScreenSizeClass.COMPACT
            WindowHeightSizeClass.MEDIUM -> ScreenSizeClass.MEDIUM
            WindowHeightSizeClass.EXPANDED -> ScreenSizeClass.EXPANDED
            else -> ScreenSizeClass.MEDIUM
        }
    }

val isCompact: Boolean
    @Composable
    get() = screenWidthClass == ScreenSizeClass.COMPACT