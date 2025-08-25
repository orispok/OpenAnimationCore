package com.osg.openanimation.core.ui.dashboard

import com.osg.openanimation.core.data.upload.ModerationStatus
import com.osg.openanimation.core.data.upload.UploadedAnimationMeta
import com.osg.openanimation.core.ui.color.model.ColorsEditPalette

sealed interface AnimationUploadUiState {
    data object Loading : AnimationUploadUiState
    data class Ready(
        val editableAnimation: ColorsEditPalette,
        val uploadedAnimationMeta: UploadedAnimationMeta,
        val moderationStatus: ModerationStatus,
        val allTags: Set<String> = emptySet(),
    ) : AnimationUploadUiState
}