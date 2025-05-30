package com.osg.appUiLayer.details

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

@Composable
fun IconButtonInstantTask(
    modifier: Modifier = Modifier,
    primaryIcon: ImageVector,
    secondaryIcon: ImageVector,
    onSuspendedClick: suspend () -> Unit,
) {
    var isCopied by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val iconColor by animateColorAsState(
        targetValue = if (isCopied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(300),
        label = "iconColorAnimation"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isCopied) 1.2f else 1.0f,
        animationSpec = tween(300),
        label = "iconScaleAnimation"
    )

    LaunchedEffect(isCopied) {
        if (isCopied) {
            delay(2000)
            isCopied = false
        }
    }

    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            if (!isLoading) {
                scope.launch {
                    isLoading = true
                    try {
                        onSuspendedClick()
                        isCopied = true
                    } finally {
                        isLoading = false
                    }
                }
            }
        },
        modifier = modifier,
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.Companion.size(24.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = if (isCopied) secondaryIcon else primaryIcon,
                contentDescription = if (isCopied) "Link Copied" else "Copy Link",
                tint = iconColor,
                modifier = Modifier.Companion.graphicsLayer(scaleX = iconScale, scaleY = iconScale)
            )
        }
    }
}