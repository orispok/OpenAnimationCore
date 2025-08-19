package com.osg.openanimation.core.ui.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.pow
import kotlin.math.roundToInt


@Composable
fun ColorsEditView(
    modifier: Modifier = Modifier,
    lottieRaw: String,
    hash: String,
) {

    val viewModel = viewModel { ColorsEditViewModel(lottieRaw, hash)  }
    val uiState by viewModel.uiState.collectAsState()
    ColorPaletteOptionsView(
        modifier = modifier,
        expanded = uiState.expanded,
        transformOptions = uiState.options,
        onPalletSelect = viewModel::onSelectColorTransformOption,
    )
}

@Composable
fun ColorPaletteOptionsView(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    transformOptions: List<List<Color>>,
    onPalletSelect: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onPalletSelect(0)}
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
                        }
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                ColorPaletteView(
                    modifier = Modifier.weight(1f),
                    transformedColors = transformOptions.firstOrNull() ?: emptyList(),
                )
                IconButton(onClick = { onPalletSelect(0) }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
                }
            }
        }
    }
}



data class TransformOptions(
    val hueShift: Float = 0f,
) {
    fun transform(color: Color): Color {
        val hslColor = color.toHsl()
        return Color.hsl(
            hue = (hslColor.hue + hueShift) % 360f,
            saturation = hslColor.saturation,
            lightness = hslColor.lightness,
            alpha = color.alpha
        )
    }

    fun transform(colors: List<Color>): List<Color> {
        return colors.map { color ->
            transform(color)
        }
    }
}


@Composable
fun ColorPaletteView(
    modifier: Modifier = Modifier,
    transformedColors: List<Color>,
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
                        .height(24.dp)
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
