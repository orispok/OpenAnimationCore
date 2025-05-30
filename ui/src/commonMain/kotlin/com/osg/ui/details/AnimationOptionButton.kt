package com.osg.appUiLayer.details

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.osg.appUiLayer.components.bar.DropDownAnimatingIcon
import com.osg.appUiLayer.components.bar.GeneralIconButtonItem
import com.osg.appUiLayer.util.icons.Flag
import com.osg.appUiLayer.components.report.AnimationReport
import openanimation.appuilayer.generated.resources.Res
import openanimation.appuilayer.generated.resources.report
import org.osg.previewopenanimation.core.data.animation.AnimationMetadata


@Composable
fun AnimationOptionButton(
    modifier: Modifier = Modifier,
    animationMetadata: AnimationMetadata,
) {
    var showReportDialog by remember { mutableStateOf(false) }
    DropDownAnimatingIcon(
        modifier = modifier,
        dropDownOptions = listOf(
            GeneralIconButtonItem(
                stringResource = Res.string.report,
                imageVector = Icons.Default.Flag,
                onClick = {
                    showReportDialog = true
                }
            )
        )
    )
    if (showReportDialog) {
        AnimationReport(
            animationMetadata = animationMetadata,
            onDismissRequest = {
                showReportDialog = false
            }
        )
    }
}

