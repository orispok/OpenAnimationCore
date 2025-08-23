package com.osg.openanimation.core.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.osg.openanimation.core.data.upload.ModerationStatus
import com.osg.openanimation.core.ui.home.domain.ColorPaletteWithMetadata

@Composable
fun UploadAnimationView(
    modifier: Modifier = Modifier,
    animationUiData: ColorPaletteWithMetadata,
    moderationStatus: ModerationStatus? = null,
    onPalletSelect: (Int) -> Unit,
    onUploadClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTagsChange: (List<String>) -> Unit,
){

}