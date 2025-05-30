package com.osg.core.ui.components.bar

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.osg.core.ui.util.resource.string
import org.jetbrains.compose.resources.StringResource

@Composable
fun DropDownAnimatingIcon(
    dropDownOptions: List<GeneralIconButtonItem>,
    modifier: Modifier = Modifier,
) {
    var isSelected by remember { mutableStateOf(false) }
    val layoutDirection = LocalLayoutDirection.current
    val gearAnimationDegree = remember {
        if (layoutDirection == LayoutDirection.Ltr) {
            90f
        } else {
            -90f
        }
    }
    val degree = animateFloatAsState(
        targetValue = if (isSelected) gearAnimationDegree else 0f,
        animationSpec = spring(
            dampingRatio = 0.3f,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "degreeAnimation"
    )

    IconButton(
        modifier = modifier,
        onClick = {isSelected = true}
    ) {
        Icon(
            modifier = Modifier.rotate(degree.value),
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = null,
        )
        DropDownMenu(dropDownOptions, isSelected) {
            isSelected = false
        }
    }
}

@Composable
fun OptionsButton(
    dropDownOptions: List<GeneralIconButtonItem>,
    imageVector: ImageVector,
) {
    var clicked by remember { mutableStateOf(false) }
    IconButton(
        modifier = Modifier,
        onClick = { clicked = true }
    ) {
        Icon(
            modifier = Modifier,
            imageVector = imageVector,
            contentDescription = null
        )
        DropDownMenu(dropDownOptions, clicked) {
            clicked = false
        }
    }
}

@Composable
fun DropDownMenu(
    buttonContents: List<GeneralIconButtonItem>,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier.Companion.animateContentSize(),
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(0.dp, 0.dp)
    ) {
        buttonContents.forEach { buttonContent ->
            DropDownItem(
                stringResource =  buttonContent.stringResource,
                imageVector = buttonContent.imageVector,
                onClick = {
                    buttonContent.onClick()
                    onDismissRequest()
                }
            )
        }
    }
}


@Composable
fun DropDownItem(
    stringResource: StringResource,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(stringResource.string) },
        leadingIcon = {
            Icon(
                imageVector = imageVector,
                contentDescription = null
            )
        },
        onClick = onClick
    )
}