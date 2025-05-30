package com.osg.openanimation.core.ui.details

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.osg.openanimation.core.ui.util.icons.Flag
import com.osg.core.components.report.AnimationReport
import com.osg.openanimation.core.ui.components.bar.DropDownAnimatingIcon
import com.osg.openanimation.core.ui.components.bar.GeneralIconButtonItem
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.ui.generated.resources.Res
import com.osg.openanimation.core.ui.generated.resources.report


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

