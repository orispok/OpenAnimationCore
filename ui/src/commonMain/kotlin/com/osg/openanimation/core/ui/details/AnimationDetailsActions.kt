package com.osg.openanimation.core.ui.details

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.ui.di.LocalLinkProvider
import com.osg.openanimation.core.ui.graph.Destination
import com.osg.openanimation.core.ui.util.icons.Download
import com.osg.openanimation.core.ui.util.icons.Link
import com.osg.openanimation.core.ui.util.link.createClipEntryWithPlainText
import kotlinx.coroutines.launch


@Composable
fun AnimationDetailsActions(
    modifier: Modifier = Modifier,
    animationMetadata: AnimationMetadata,
    isLiked: Boolean,
    isDownloadedTransition: Boolean,
    onLikeClick: (Boolean) -> Boolean,
    onDownloadClick: (AnimationMetadata) -> Unit,
){
    Row(
        modifier = modifier,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.85f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "icon_button_scale"
        )
        var isLikedLocal by remember(isLiked) { mutableStateOf(isLiked) }
        IconButton(
            onClick = {
                isLikedLocal = onLikeClick(isLikedLocal.not())
            },
            interactionSource = interactionSource,
            modifier = modifier,
        ) {
            Icon(
                imageVector = if (isLikedLocal) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isLikedLocal) "Unlike" else "Like",
                tint = if (isLikedLocal) MaterialTheme.colorScheme.error else LocalContentColor.current,
                modifier = Modifier.scale(scale)
            )
        }
        DownloadButton(
            modifier = Modifier.size(48.dp),
            isDownloadedTransition = isDownloadedTransition,
            onClick = {
                onDownloadClick(animationMetadata)
            }
        )
        val linkProvider = LocalLinkProvider.current
        CopyLinkToClipboardButton(
            modifier = Modifier.size(48.dp),
            generateLink = {
                linkProvider.windowUrl(
                    Destination.AnimationDetails(
                        hash = animationMetadata.hash
                    )
                )
            }
        )
        AnimationOptionButton(
            animationMetadata = animationMetadata,
        )
    }
}

@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    isDownloadedTransition: Boolean,
    onClick: () -> Unit,
) {
    var iconAnimationState by remember { mutableStateOf(IconAnimationState.IDLE) }
    LaunchedEffect(isDownloadedTransition) {
        if (isDownloadedTransition){
            iconAnimationState = IconAnimationState.LOADING
        }else if(iconAnimationState == IconAnimationState.LOADING){
            iconAnimationState = IconAnimationState.Done
        }
    }
    IconButtonInstantTask(
        modifier = modifier,
        primaryIcon = Icons.Default.Download,
        secondaryIcon = Icons.Default.Check,
        iconAnimationState = iconAnimationState,
        onReturnToIdle = {
            iconAnimationState = IconAnimationState.IDLE
        },
        onClick = onClick
    )
}

@Composable
fun CopyLinkToClipboardButton(
    modifier: Modifier = Modifier,
    generateLink: () -> String,
) {
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()
    var iconAnimationState by remember { mutableStateOf(IconAnimationState.IDLE) }
    IconButtonInstantTask(
        modifier = modifier,
        primaryIcon = Icons.Default.Link,
        secondaryIcon = Icons.Default.Check,
        iconAnimationState = iconAnimationState,
        onReturnToIdle = {
            iconAnimationState = IconAnimationState.IDLE
        },
        onClick = {
            iconAnimationState = IconAnimationState.LOADING
            scope.launch {
                val clipEntry = createClipEntryWithPlainText(
                    text = generateLink(),
                )
                clipboardManager.setClipEntry(clipEntry)
                iconAnimationState = IconAnimationState.Done
            }
        }
    )
}