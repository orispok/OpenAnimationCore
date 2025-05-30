package com.osg.openanimation.core.ui.util.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity

@Stable
@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }