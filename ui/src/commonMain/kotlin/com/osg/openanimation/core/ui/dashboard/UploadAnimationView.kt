package com.osg.openanimation.core.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.data.upload.ModerationStatus
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.ui.color.model.ColorsEditPalette
import com.osg.openanimation.core.ui.color.ui.ColorPaletteOptionsView
import com.osg.openanimation.core.ui.components.loading.LoadingAnimation
import com.osg.openanimation.core.ui.components.lottie.AnimationCard
import com.osg.openanimation.core.ui.util.adaptive.pxToDp
import kotlin.math.roundToInt


@Composable
fun AnimationUploadScreen(
    modifier: Modifier = Modifier,
    uiState: AnimationUploadUiState,
    onPaletteSelected: (Int) -> Unit,
    onUploadClick: () -> Unit,
    onRemovalClick: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onTagsChanged: (Set<String>) -> Unit,
) {
    when (uiState) {
        is AnimationUploadUiState.Loading -> {
            LoadingAnimation(
                modifier = Modifier.fillMaxSize()
            )
        }

        is AnimationUploadUiState.Ready -> {
            AnimationUploadForm(
                modifier = modifier,
                uploadedAnimationMeta = uiState.uploadedAnimationMeta,
                moderationStatus = uiState.moderationStatus,
                onPaletteSelected = onPaletteSelected,
                onUploadClick = onUploadClick,
                onTitleChanged = onTitleChanged,
                onTagsChanged = onTagsChanged,
                editableAnimation = uiState.editableAnimation,
                allTags = uiState.allTags,
                onRemovalClick = onRemovalClick
            )
        }
    }
}

@Composable
fun AnimationUploadForm(
    modifier: Modifier = Modifier,
    editableAnimation: ColorsEditPalette,
    uploadedAnimationMeta: UploadedAnimationMeta,
    moderationStatus: ModerationStatus,
    allTags: Set<String>,
    onPaletteSelected: (Int) -> Unit,
    onUploadClick: () -> Unit,
    onRemovalClick: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onTagsChanged: (Set<String>) -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val containerSize = LocalWindowInfo.current.containerSize
        val animationHeight = (containerSize.height * 0.6).roundToInt()
        AnimationCard(
            modifier = Modifier.height(animationHeight.pxToDp()),
            animationState = editableAnimation.processedJsonState,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedTextField(
                modifier = Modifier,
                value = uploadedAnimationMeta.name,
                onValueChange = onTitleChanged,
                label = { Text("Title") },
                maxLines = 1,
                singleLine = true,
            )
            ColorPaletteOptionsView(
                modifier = Modifier.width(120.dp),
                transformOptions = editableAnimation.options,
                selectedIndex = editableAnimation.selectedOptionIndex,
                onPalletSelect = onPaletteSelected,
            )
        }
        TagsEditView(
            tags = uploadedAnimationMeta.tags.toSet(),
            onTagsChange = onTagsChanged,
            allTags = allTags,
        )
        UploadController(moderationStatus, onUploadClick, onRemovalClick)
    }
}

@Composable
private fun UploadController(
    moderationStatus: ModerationStatus,
    onUploadClick: () -> Unit,
    onRemovalClick: () -> Unit
) {
    var isEnabled by remember(moderationStatus) { mutableStateOf(true) }
    if (moderationStatus == ModerationStatus.DRAFT) {
        Button(
            onClick = {
                isEnabled = false
                onUploadClick()
            },
            modifier = Modifier,
            enabled = isEnabled
        ) {
            Text("Upload Animation")
        }
    } else {
        ModerationStatusView(moderationStatus)
    }
    Button(
        onClick = {
            isEnabled = false
            onRemovalClick()
        },
        modifier = Modifier,
        enabled = isEnabled
    ) {
        Text("Remove Animation")
    }
}


@Composable
fun ModerationStatusView(
    status: ModerationStatus,
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier,
        imageVector = when (status) {
            ModerationStatus.DRAFT -> Icons.Default.Info
            ModerationStatus.PENDING -> Icons.Default.Info
            ModerationStatus.APPROVED -> Icons.Default.CheckCircle
            ModerationStatus.REJECTED -> Icons.Default.ThumbUp
        },
        contentDescription = status.name,
        tint = when (status) {
            ModerationStatus.DRAFT -> MaterialTheme.colorScheme.onSurface
            ModerationStatus.PENDING -> MaterialTheme.colorScheme.onSurface
            ModerationStatus.APPROVED -> MaterialTheme.colorScheme.primary
            ModerationStatus.REJECTED -> MaterialTheme.colorScheme.error
        },
    )
}