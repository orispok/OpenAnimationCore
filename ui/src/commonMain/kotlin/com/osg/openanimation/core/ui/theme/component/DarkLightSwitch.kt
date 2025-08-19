package com.osg.openanimation.core.ui.theme.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.osg.openanimation.core.ui.components.button.noRippleClickable
import com.osg.openanimation.core.ui.components.loading.animationResource
import com.osg.openanimation.core.ui.components.lottie.ClipSpecProgress
import com.osg.openanimation.core.ui.components.lottie.lottieClippedAnimation

@Composable
fun DarkLightSwitch(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    onSwitch: (Boolean) -> Unit,
) {

    Box(
        modifier = modifier,
    ) {
        val animation = animationResource("mode_switch_animation.json")

        var clipIdx by remember { mutableIntStateOf(if (isDarkMode) 0 else 1) }
        val painter = lottieClippedAnimation(
            animation,
            listOf(
                ClipSpecProgress(0f, 0.6f),
                ClipSpecProgress(0.6f, 1f),
            ),
            clipIdx = clipIdx,
            onProgressChanged = { progress ->
                if (progress in 0.2f..0.3f) {
                    onSwitch(true)
                } else if (progress in 0.7f..8f) {
                    onSwitch(false)
                }
            }
        )
        Image(
            modifier = Modifier.fillMaxSize().noRippleClickable(
                onClick = {
                    clipIdx += 1
                }
            ),
            painter = painter,
            contentDescription = "Lottie animation",
            contentScale = ContentScale.Fit
        )
    }
}
