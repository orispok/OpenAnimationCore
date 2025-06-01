package com.osg.openanimation.core.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.osg.openanimation.core.ui.components.loading.LoadingAnimation
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialog
import com.osg.openanimation.core.ui.file.exportService

sealed interface DialogType {
    data object SignInDialog : DialogType
    sealed interface Export: DialogType{
        data class Success(
            val animationData: String,
            val fileName: String,
        ) : Export
    }
}

@Composable
fun AnimationDialogTypeView(
    dialogType: DialogType,
    onDismissRequest: () -> Unit,
) {
    when (dialogType) {
        is DialogType.Export.Success -> {
            exportService.ExportFile(
                dataString = dialogType.animationData,
                fileName = dialogType.fileName,
                onFinished = onDismissRequest,
            )
        }
        is DialogType.SignInDialog -> {
            SignInReasoningDialog(
                onDismissRequest = onDismissRequest,
            )
        }
    }
}
