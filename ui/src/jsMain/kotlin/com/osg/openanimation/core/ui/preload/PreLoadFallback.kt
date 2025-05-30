package com.osg.openanimation.core.ui.preload

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import com.osg.openanimation.core.ui.theme.commonFontResource
import com.osg.openanimation.core.ui.generated.resources.Res
import com.osg.openanimation.core.ui.generated.resources.noto_color_emoji
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PreLoadFallback(
    content: @Composable () -> Unit
) {
    val fontsResource =  listOf(commonFontResource, Res.font.noto_color_emoji)

    val fontStates = fontsResource.map{
        preloadFont(it).value
    }

    val fontFamilyResolver = LocalFontFamilyResolver.current
    var isPreloadComplete by remember { mutableStateOf(false) }
    LaunchedEffect(fontFamilyResolver, fontStates) {
        fontStates.forEach { fontState ->
            fontState?.let { font ->
                fontFamilyResolver.preload(FontFamily(font))
            }
        }

        if (fontStates.any { it != null }) {
            isPreloadComplete = true
        }
    }
    if (isPreloadComplete) {
        content()
    }
}