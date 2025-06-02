package com.osg.openanimation.core.ui.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class IconAnimationState {
    IDLE,
    LOADING,
    Done
}

@Composable
fun IconButtonInstantTask(
    modifier: Modifier = Modifier,
    primaryIcon: ImageVector,
    secondaryIcon: ImageVector,
    iconAnimationState: IconAnimationState,
    onReturnToIdle: () -> Unit,
    onClick: () -> Unit,
) {

    LaunchedEffect(iconAnimationState) {
        if (iconAnimationState == IconAnimationState.Done) {
            delay(2000)
            onReturnToIdle()
        }
    }

    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = iconAnimationState != IconAnimationState.LOADING
    ) {
        AnimatedContent(
            targetState = iconAnimationState,
            label = "IconButtonContent",
        ) { state ->
            when (state) {
                IconAnimationState.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }
                else -> {
                    val isDone = state == IconAnimationState.Done
                    val iconColor by animateColorAsState(
                        targetValue = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        animationSpec = tween(300),
                        label = "iconColorAnimation"
                    )

                    val iconScale by animateFloatAsState(
                        targetValue = if (isDone) 1.2f else 1.0f,
                        animationSpec = tween(300),
                        label = "iconScaleAnimation"
                    )
                    Icon(
                        imageVector = if (isDone) secondaryIcon else primaryIcon,
                        contentDescription = if (isDone) "Link Copied" else "Copy Link",
                        tint = iconColor,
                        modifier = Modifier.graphicsLayer(scaleX = iconScale, scaleY = iconScale)
                    )
                }
            }
        }
    }
}
