package com.osg.openanimation.core.ui.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ColorsEditView(
    modifier: Modifier = Modifier,
    lottieRaw: String,
) {
    var colors by remember { mutableStateOf(emptyList<StaticColorNode>()) }



    LaunchedEffect(lottieRaw) {
        colors = extractColorsFromJson(lottieRaw)
    }

    Row(
        modifier = modifier
    ){
        colors.map { it.color }.distinct().forEach { colorNode ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .weight(1f)
                    .background(colorNode)
            )
        }
    }
}