package com.osg.openanimation.core.ui.color.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.pow
import kotlin.math.roundToInt


@Composable
fun ColorPaletteOptionsView(
    modifier: Modifier = Modifier,
    transformOptions: ImmutableList<ImmutableList<Color>>,
    selectedIndex: Int,
    onPalletSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier,
    ) {
        if (transformOptions.isNotEmpty()) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorPaletteView(
                    modifier = Modifier.weight(1f),
                    transformedColors = transformOptions[selectedIndex],
                )
                IconButton(onClick = {
                    expanded = true
                }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
                }
            }
            if (expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    transformOptions.forEachIndexed { index, it ->
                        DropdownMenuItem(
                            text = {
                                ColorPaletteView(
                                    modifier = Modifier,
                                    transformedColors = it,
                                )
                            },
                            onClick = {
                                onPalletSelect(index)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ColorPaletteView(
    modifier: Modifier = Modifier,
    transformedColors: ImmutableList<Color>,
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
        ) {
            transformedColors.forEach { colorNode ->
                Spacer(
                    modifier = Modifier.size(1.dp)
                )
                Box(
                    modifier = Modifier
                        .height(18.dp)
                        .weight(1f)
                        .background(colorNode)
                )
            }
        }
    }

}

fun Float.roundTo(numFractionDigits: Int): Float {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return ((this * factor).roundToInt() / factor).toFloat()
}
