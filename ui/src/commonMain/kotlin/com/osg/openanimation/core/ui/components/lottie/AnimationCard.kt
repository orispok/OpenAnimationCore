package com.osg.openanimation.core.ui.components.lottie

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
import com.osg.openanimation.core.data.animation.AnimationMetadata
import com.osg.openanimation.core.ui.details.AnimationOptionButton
import com.osg.openanimation.core.ui.home.domain.AnimationUiData


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
            .background(MaterialTheme.colorScheme.tertiaryContainer)
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
        leadingContent = {},
        trailingContent = {
            AnimationOptionButton(
                animationMetadata = animationMetadata
            )

        },
        modifier = modifier
    )
}