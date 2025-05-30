package com.osg.appUiLayer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.osg.appUiLayer.details.AnimationOptionButton
import com.osg.appUiLayer.components.lottie.AnimationDataState
import com.osg.appUiLayer.components.lottie.LottieAnimationView
import com.osg.appUiLayer.home.domain.AnimationUiData
import org.osg.previewopenanimation.core.data.animation.AnimationMetadata


@Composable
fun AnimationCaptionCard(
    animationData: AnimationUiData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    Column(
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimationCard(
            modifier = Modifier
                .weight(1f),
            animationState = animationData.animationState,
            onClick = onClick
        )
        AnimationQuickActions(
            modifier = Modifier,
            animationMetadata = animationData.metadata
        )
    }
}

@Composable
fun AnimationCard(
    modifier: Modifier = Modifier,
    animationState: AnimationDataState,
    onClick: () -> Unit = {},
){
    LottieAnimationView(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick),
        animationData = animationState,
        contentScale = ContentScale.Fit
    )
}

@Composable
fun AnimationQuickActions(
    modifier: Modifier = Modifier,
    animationMetadata: AnimationMetadata
) {
    ListItem(
        headlineContent = { Text(animationMetadata.name) },
//        supportingContent = { Text("1.3k downloads") },
        leadingContent = {
//            Icon(
//                imageVector = Icons.Default.Face,
//                contentDescription = "profile",
//            )
        },
        trailingContent = {
            AnimationOptionButton(
                animationMetadata = animationMetadata
            )

        },
        modifier = modifier
    )
}