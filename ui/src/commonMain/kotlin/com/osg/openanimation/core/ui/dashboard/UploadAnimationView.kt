package com.osg.openanimation.core.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.data.upload.ModerationStatus
import com.osg.openanimation.core.ui.color.ui.ColorPaletteOptionsView
import com.osg.openanimation.core.ui.components.lottie.AnimationCard
import com.osg.openanimation.core.ui.home.domain.ColorPaletteWithMetadata


sealed interface AnimationUploadUiState {
    data object Loading : AnimationUploadUiState
    data class Ready(
        val animationUiData: ColorPaletteWithMetadata,
        val moderationStatus: ModerationStatus,
    ) : AnimationUploadUiState
}

@Composable
fun AnimationUploadScreen(
    modifier: Modifier = Modifier,
    uiState: AnimationUploadUiState,
    onPaletteSelected: (Int) -> Unit,
    onUploadClick: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onTagsChanged: (Set<String>) -> Unit,
){
    when(uiState){
        is AnimationUploadUiState.Loading -> {
            // Show loading state
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Loading...")
            }
        }
        is AnimationUploadUiState.Ready -> {
            AnimationUploadForm(
                modifier = modifier,
                animationUiData = uiState.animationUiData,
                moderationStatus = uiState.moderationStatus,
                onPaletteSelected = onPaletteSelected,
                onUploadClick = onUploadClick,
                onTitleChanged = onTitleChanged,
                onTagsChanged = onTagsChanged
            )
        }
    }
}
@Composable
fun AnimationUploadForm(
    modifier: Modifier = Modifier,
    animationUiData: ColorPaletteWithMetadata,
    moderationStatus: ModerationStatus,
    onPaletteSelected: (Int) -> Unit,
    onUploadClick: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onTagsChanged: (Set<String>) -> Unit,
){
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        AnimationCard(
            modifier = Modifier,
            animationState = animationUiData.editableAnimation.processedJsonState,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextField(
                modifier = Modifier,
                value = animationUiData.metadata.name,
                onValueChange = onTitleChanged,
                label = { Text("Title") },
                maxLines = 1,
                singleLine = true,
            )
            ColorPaletteOptionsView(
                modifier = Modifier.width(120.dp),
                transformOptions = animationUiData.editableAnimation.options,
                selectedIndex = animationUiData.editableAnimation.selectedOptionIndex,
                onPaletteSelected = onPaletteSelected
            )
        }
        TagsEditView(
            tags = animationUiData.metadata.tags,
            onTagsChanged = onTagsChanged
        )
        if(moderationStatus == ModerationStatus.DRAFT){
            Button(
                onClick = onUploadClick,
                modifier = Modifier
            ) {
                Text("Upload Animation")
            }
        }
    }
}