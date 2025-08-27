package com.osg.openanimation.core.ui.preload

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import com.osg.openanimation.core.ui.generated.resources.Res
import com.osg.openanimation.core.ui.generated.resources.allFontResources
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.preloadFont


@OptIn(ExperimentalResourceApi::class)
@Composable
fun PreLoadFallback(
    extraFonts: List<FontResource> = emptyList(),
    content: @Composable () -> Unit
) {
    val fontsResource = extraFonts + Res.allFontResources.values

    val fontStates = fontsResource.mapNotNull {
        preloadFont(it).value
    }

    if (fontStates.size != fontsResource.size) {
        return
    }

    val fontFamilyResolver = LocalFontFamilyResolver.current
    var isPreloaded by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(fontFamilyResolver) {
        fontStates.forEach { font ->
            fontFamilyResolver.preload(FontFamily(font))
        }
        isPreloaded = true
    }

    if (isPreloaded) {
        content()
    }
}
