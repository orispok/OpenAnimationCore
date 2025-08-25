package com.osg.openanimation.core.ui.dashboard

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.data.upload.ModerationStatus
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.ui.color.model.ColorsEditPalette
import com.osg.openanimation.core.ui.color.ui.ColorPaletteOptionsView
import com.osg.openanimation.core.ui.components.loading.LoadingAnimation
import com.osg.openanimation.core.ui.components.lottie.AnimationCard
import com.osg.openanimation.core.ui.util.adaptive.pxToDp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun AnimationUploadScreen(
    modifier: Modifier = Modifier,
    uiState: AnimationUploadUiState,
    onPaletteSelected: (Int) -> Unit,
    onUploadClick: () -> Unit,
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
        if (moderationStatus == ModerationStatus.DRAFT) {
            Button(
                onClick = onUploadClick,
                modifier = Modifier
            ) {
                Text("Upload Animation")
            }
        } else {
            ModerationStatusView(moderationStatus)
        }
        Button(
            onClick = { /* TODO: */ },
            modifier = Modifier
        ) {
            Text("Remove")
        }
    }
}

@Composable
fun ModerationStatusView(
    status: ModerationStatus,
    startColor: Color = MaterialTheme.colorScheme.onSurface,
    endColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
) {
    val statuses = ModerationStatus.entries
    val currentStatusIndex = statuses.indexOf(status)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        statuses.forEachIndexed { index, moderationStatus ->
            val colorAnim = remember { Animatable(startColor) }
            LaunchedEffect(currentStatusIndex) {
                if (index <= currentStatusIndex) {
                    delay(250.milliseconds * index)
                    colorAnim.animateTo(
                        targetValue = endColor,
                        animationSpec = tween(250)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    imageVector = when (moderationStatus) {
                        ModerationStatus.DRAFT -> Icons.Default.Info
                        ModerationStatus.PENDING -> Icons.Default.Info
                        ModerationStatus.APPROVED -> Icons.Default.CheckCircle
                        ModerationStatus.REJECTED -> Icons.Default.ThumbUp
                    },
                    contentDescription = moderationStatus.name,
                    tint = colorAnim.value
                )
                Text(
                    text = moderationStatus.name.lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    color = colorAnim.value
                )
            }
        }
    }
}