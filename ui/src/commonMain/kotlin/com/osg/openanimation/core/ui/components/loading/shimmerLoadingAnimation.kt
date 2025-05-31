package com.osg.openanimation.core.ui.components.loading
import androidx.compose.animation.core.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


fun Modifier.shimmerLoadingAnimation(
    runShimmer: Boolean,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    cornerSize: Dp = 0.dp
): Modifier {
    if (runShimmer.not()) {
        return this
    } else {
        return composed {
            val shimmerColors = listOf(
                Color.Companion.White.copy(alpha = 0.3f),
                Color.Companion.White.copy(alpha = 0.5f),
                Color.Companion.White.copy(alpha = 1.0f),
                Color.Companion.White.copy(alpha = 0.5f),
                Color.Companion.White.copy(alpha = 0.3f),
            )
            val cornerSizePx = with(LocalDensity.current) { cornerSize.toPx() }
            val transition = rememberInfiniteTransition(label = "")

            val translateAnimation = transition.animateFloat(
                initialValue = 0f,
                targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "Shimmer loading animation",
            )
            this.drawWithContent {
                drawContent()
                // draws a fully black area with a small keyhole at pointerOffset that’ll show part of the UI.
                drawRoundRect(
                    brush = Brush.Companion.linearGradient(
                        colors = shimmerColors,
                        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
                    ),
                    cornerRadius = CornerRadius(cornerSizePx, cornerSizePx),
                )
            }
        }
    }
}