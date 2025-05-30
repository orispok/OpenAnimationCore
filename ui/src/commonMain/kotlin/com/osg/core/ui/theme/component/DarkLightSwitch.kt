package com.osg.core.ui.theme.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.osg.core.ui.components.button.noRippleClickable
import com.osg.core.components.lottie.ClipSpecProgress
import com.osg.core.ui.components.lottie.AnimationDataState
import com.osg.core.ui.components.lottie.lottieClippedAnimation

@Composable
fun DarkLightSwitch(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    onSwitch: (Boolean) -> Unit,
) {
    val animation = AnimationDataState.fromResource("files/mode_switch_animation.json")
    var clipIdx by remember { mutableIntStateOf(if (isDarkMode) 0 else 1) }
    val painter = lottieClippedAnimation(
        animation,
        listOf(
            ClipSpecProgress(0f, 0.6f),
            ClipSpecProgress(0.6f, 1f),
        ),
        clipIdx = clipIdx,
        onProgressChanged = { progress ->
            if(progress in 0.2f..0.3f) {
                onSwitch(true)
            } else if (progress in 0.7f..8f) {
                onSwitch(false)
            }
        }
    )
    Image(
        modifier = modifier.noRippleClickable(
            onClick = {
                clipIdx += 1
            }
        ),
        painter = painter,
        contentDescription = "Lottie animation",
        contentScale = ContentScale.Fit
    )
}
