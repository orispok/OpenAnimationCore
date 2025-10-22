package com.osg.openanimation.core.ui.util.adaptive

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable

enum class ScreenSizeClass {
    COMPACT,
    MEDIUM,
    EXPANDED
}

val currentScreenWidthClass: ScreenSizeClass
    @Composable
    get() {
//        return ScreenSizeClass.MEDIUM
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        return if(windowSizeClass.isWidthAtLeastBreakpoint(840)){
            ScreenSizeClass.EXPANDED
        } else if(windowSizeClass.isWidthAtLeastBreakpoint(600)){
            ScreenSizeClass.MEDIUM
        } else {
            ScreenSizeClass.COMPACT
        }
    }

val isCompactWidth: Boolean
    @Composable
    get() = currentScreenWidthClass == ScreenSizeClass.COMPACT