package com.osg.openanimation.core.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.osg.openanimation.core.ui.util.adaptive.isCompactWidth
import com.osg.openanimation.core.ui.util.adaptive.pxToDp
import com.osg.openanimation.core.ui.util.icons.Save
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
    onSaveClick: () -> Unit,
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
                onRemovalClick = onRemovalClick,
                onSaveClick = onSaveClick
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
    onSaveClick: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onTagsChanged: (Set<String>) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ColorPaletteOptionsView(
                modifier = Modifier.width(120.dp),
                transformOptions = editableAnimation.options,
                selectedIndex = editableAnimation.selectedOptionIndex,
                onPalletSelect = onPaletteSelected,
            )
            UploadController(
                moderationStatus = moderationStatus,
                onUploadClick = onUploadClick,
                onRemovalClick = onRemovalClick,
                onSaveClick = onSaveClick
            )
        }
        if (isCompactWidth) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
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
                EditableFieldsView(
                    modifier = Modifier,
                    uploadedAnimationMeta = uploadedAnimationMeta,
                    onTitleChanged = onTitleChanged,
                    onTagsChanged = onTagsChanged,
                    allTags = allTags,
                    moderationStatus = moderationStatus
                )
            }
        } else {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center,
            ) {
                AnimationCard(
                    modifier = Modifier.weight(1f),
                    animationState = editableAnimation.processedJsonState,
                )

                EditableFieldsView(
                    modifier = Modifier,
                    uploadedAnimationMeta = uploadedAnimationMeta,
                    onTitleChanged = onTitleChanged,
                    onTagsChanged = onTagsChanged,
                    allTags = allTags,
                    moderationStatus = moderationStatus
                )
            }
        }
    }
}

@Composable
private fun EditableFieldsView(
    modifier: Modifier = Modifier,
    moderationStatus: ModerationStatus,
    uploadedAnimationMeta: UploadedAnimationMeta,
    onTitleChanged: (String) -> Unit,
    onTagsChanged: (Set<String>) -> Unit,
    allTags: Set<String>
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ElevatedCard {
            Text(
                modifier = Modifier.padding(8.dp),
                text = moderationStatus.name
            )
        }
        OutlinedTextField(
            modifier = Modifier,
            value = uploadedAnimationMeta.name,
            onValueChange = onTitleChanged,
            label = { Text("Title") },
            maxLines = 1,
            singleLine = true,
        )
        TagsEditView(
            tags = uploadedAnimationMeta.tags.toSet(),
            onTagsChange = onTagsChanged,
            allTags = allTags,
        )
    }

}

@Composable
private fun UploadController(
    moderationStatus: ModerationStatus,
    onUploadClick: () -> Unit,
    onRemovalClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    var isEnabled by remember(moderationStatus) { mutableStateOf(true) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {

        IconButton(
            onClick = {
                isEnabled = false
                onRemovalClick()
            },
            modifier = Modifier,
            enabled = isEnabled
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove Animation",
            )
        }

        IconButton(
            onClick = {
                isEnabled = false
                onSaveClick()
            },
            modifier = Modifier,
            enabled = isEnabled
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Remove Animation",
            )
        }

        if (moderationStatus == ModerationStatus.DRAFT) {
            ElevatedButton(
                onClick = {
                    isEnabled = false
                    onUploadClick()
                },
                modifier = Modifier,
                enabled = isEnabled
            ) {
                Text("Publish")
            }
        }
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