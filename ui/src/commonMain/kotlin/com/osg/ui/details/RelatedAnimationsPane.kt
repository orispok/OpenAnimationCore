package com.osg.appUiLayer.details

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.osg.appUiLayer.AnimationCaptionCard
import com.osg.appUiLayer.home.domain.AnimationUiData

@Composable
fun RelatedAnimationsPane(
    modifier: Modifier = Modifier,
    relatedAnimations: List<AnimationUiData>,
    onAnimationClicked: (AnimationUiData) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(relatedAnimations) { animation ->
            AnimationCaptionCard(
                modifier = Modifier.size(height = 300.dp, width = 250.dp),
                animationData = animation,
                onClick = {
                    onAnimationClicked(animation)
                },
            )
        }
    }
}