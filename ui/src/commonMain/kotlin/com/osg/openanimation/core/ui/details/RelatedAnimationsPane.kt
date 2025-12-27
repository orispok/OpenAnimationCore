package com.osg.openanimation.core.ui.details

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.ui.components.lottie.AnimationCaptionCard
import com.osg.openanimation.core.ui.home.domain.AnimationUiData
import kotlinx.collections.immutable.ImmutableList

@Composable
fun RelatedAnimationsPane(
    modifier: Modifier = Modifier,
    relatedAnimations: ImmutableList<AnimationUiData>,
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