@file:OptIn(ExperimentalTime::class)

package com.osg.openanimation.core.ui.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.ui.di.LocalLinkProvider
import com.osg.openanimation.core.ui.components.license.description
import com.osg.openanimation.core.ui.components.lottie.AnimationCard
import com.osg.openanimation.core.ui.graph.Destination
import com.osg.openanimation.core.ui.home.domain.AnimationUiData
import com.osg.openanimation.core.ui.util.icons.Download
import com.osg.openanimation.core.ui.util.icons.Link
import com.osg.openanimation.core.ui.util.adaptive.isCompact
import com.osg.openanimation.core.ui.util.adaptive.pxToDp
import com.osg.openanimation.core.ui.util.link.createClipEntryWithPlainText
import com.osg.openanimation.core.ui.util.time.fromEpochToDayDateFormat
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.ui.components.loading.shimmerLoadingAnimation
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.time.ExperimentalTime


@Composable
fun AnimationDetailsPanes(
    modifier: Modifier = Modifier,
    detailsUiState: DetailsScreenStates.Success,
    onRelatedAnimationClicked: (AnimationUiData) -> Unit,
    onLikeClick: (Boolean) -> Unit,
    onDownloadClick: (AnimationMetadata) -> Unit,
    onDismissSignInDialog: () -> Unit,
    onTagClick: (String) -> Unit,
) {
    if (isCompact) {
        AnimationDetailsView(
            modifier = modifier.padding(horizontal = 16.dp),
            detailsUiState = detailsUiState.detailsUiPane,
            onLikeClick = onLikeClick,
            onTagClick = onTagClick,
            onDownloadClick = onDownloadClick,
            onDismissSignInDialog = onDismissSignInDialog,
        )
    }else{
        Row(
            modifier = modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            AnimationDetailsView(
                modifier = Modifier.weight(1f),
                detailsUiState = detailsUiState.detailsUiPane,
                onLikeClick = onLikeClick,
                onTagClick = onTagClick,
                onDownloadClick = onDownloadClick,
                onDismissSignInDialog = onDismissSignInDialog,
            )
            RelatedAnimationsPane(
                modifier = Modifier,
                relatedAnimations = detailsUiState.relatedAnimations,
                onAnimationClicked = onRelatedAnimationClicked
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationDetailsView(
    modifier: Modifier = Modifier,
    detailsUiState: DetailsUiPane,
    onDismissSignInDialog: () -> Unit,
    onLikeClick: (Boolean) -> Unit,
    onDownloadClick: (AnimationMetadata) -> Unit,
    onTagClick: (String) -> Unit
) {
    detailsUiState.dialogToShow?.let { dialogType ->
        AnimationDialogTypeView(
            dialogType = dialogType,
            onDismissRequest = onDismissSignInDialog
        )
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        AnimationDetailsActions(
            modifier = Modifier.align(Alignment.End),
            isLiked = detailsUiState.isLiked,
            animationMetadata = detailsUiState.animationUiData.metadata,
            onLikeClick = {
                onLikeClick(detailsUiState.isLiked)
            },
            onDownloadClick = onDownloadClick,
            isDownloadedTransition = detailsUiState.isDownloadTransition,
            isLikeTransition = detailsUiState.isLikeTransition,
        )
        val containerSize = LocalWindowInfo.current.containerSize
        val animationHeight = (containerSize.height * 0.6).roundToInt()
        AnimationCard(
            modifier = Modifier.height(animationHeight.pxToDp()),
            animationState = detailsUiState.animationUiData.animationState,
        )
        Text(
            text = detailsUiState.animationUiData.metadata.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
        )
        FlowRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            detailsUiState.animationUiData.metadata.tags.forEach { tag ->
                SuggestionChip(
                    onClick = { onTagClick(tag) },
                    label = { Text(tag) }
                )
            }
        }
        Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (detailsUiState.animationStats.downloadCount > 0) {
                AnimationSpecItem(title = "⬇", value = detailsUiState.animationStats.downloadCount.toString())

            }
            if (detailsUiState.animationStats.likeCount > 0) {
                AnimationSpecItem(title = "❤", value = detailsUiState.animationStats.likeCount.toString())
            }
            AnimationSpecItem(title = "📑", value = detailsUiState.animationUiData.metadata.sizeBytes.toHumanReadableSize())
            AnimationSpecItem(title = "📆", value = detailsUiState.animationUiData.metadata.uploadedDate.fromEpochToDayDateFormat())
            AnimationSpecItem(title = "⚠", value = detailsUiState.animationUiData.metadata.license.description)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AnimationDetailsActions(
    modifier: Modifier = Modifier,
    animationMetadata: AnimationMetadata,
    isLiked: Boolean,
    isLikeTransition: Boolean,
    isDownloadedTransition: Boolean,
    onLikeClick: () -> Unit,
    onDownloadClick: (AnimationMetadata) -> Unit,
){
    Row(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier,
            onClick = onLikeClick, enabled = isLikeTransition.not()
        ) {
            AnimatedContent(
                targetState = isLikeTransition,
            ){ isTransition ->
                if (isTransition){
                    CircularProgressIndicator(
                        modifier = Modifier.Companion.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }else{
                    Icon(
                        modifier = Modifier,
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isLiked) "Unlike" else "Like",
                        tint = if (isLiked) MaterialTheme.colorScheme.error else LocalContentColor.current
                    )
                }
            }
        }
        IconButton(
            onClick = { onDownloadClick(animationMetadata) },
            enabled = isDownloadedTransition.not()
        ) {
            AnimatedContent(
                targetState = isDownloadedTransition,
            ){ isTransition ->
                if (isTransition){
                    CircularProgressIndicator(
                        modifier = Modifier.Companion.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }else{
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download Animation",
                    )
                }
            }
        }
        val clipboardManager = LocalClipboard.current
        val linkProvider = LocalLinkProvider.current
        IconButtonInstantTask(
            primaryIcon = Icons.Default.Link,
            secondaryIcon = Icons.Default.Check,
            onSuspendedClick = {
                val link = linkProvider.windowUrl(Destination.AnimationDetails(animationMetadata.hash))
                val clipEntry = createClipEntryWithPlainText(
                    text = link,
                )
                clipboardManager.setClipEntry(clipEntry)
            }
        )
        AnimationOptionButton(
            animationMetadata = animationMetadata,
        )
    }
}

@Composable
private fun AnimationSpecItem(title: String, value: String) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

fun Long.toHumanReadableSize(
    decimalPlaces: Int = 2,
    zeroPadFraction: Boolean = false
): String {
    return if (this < 1024) {
        "$this Bytes"
    } else {
        val zeroBitCount: Int = (63 - this.countLeadingZeroBits()) / 10
        val absNumber: Double = this.toDouble() / (1L shl zeroBitCount * 10)
        val roundingFactor: Int = 10.0.pow(decimalPlaces).toInt()
        val absRoundedNumberString = with((absNumber * roundingFactor).roundToLong().toString()) {
            val splitIndex = length - decimalPlaces - 1
            val wholeString = substring(0..splitIndex)
            val fractionString = with(substring(splitIndex + 1)) {
                if (zeroPadFraction) this else dropLastWhile { digit -> digit == '0' }
            }
            if (fractionString.isEmpty()) wholeString else "$wholeString.$fractionString"
        }
        val roundedNumberString = absRoundedNumberString
        "$roundedNumberString ${"KMGTPE"[zeroBitCount - 1]}B"
    }
}