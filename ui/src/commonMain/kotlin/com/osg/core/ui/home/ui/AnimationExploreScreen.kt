package com.osg.core.ui.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.osg.core.di.data.SelectedQueryType
import com.osg.core.ui.graph.Destination
import com.osg.core.ui.home.domain.ExploreScreenStates
import com.osg.core.ui.components.lottie.AnimationCaptionCard
import com.osg.core.ui.util.adaptive.ScreenSizeClass
import com.osg.core.ui.util.adaptive.screenWidthClass


@Composable
fun AnimationGrid(
    screenData: ExploreScreenStates.GridData,
    onDestination: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    val columnCount = when(screenWidthClass){
        ScreenSizeClass.COMPACT -> 1
        ScreenSizeClass.MEDIUM -> 2
        ScreenSizeClass.EXPANDED -> 3
    }
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(columnCount),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            CategoryChips(
                modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                onTagClicked = {
                    onDestination(
                        Destination.Home(
                            selectedQueryType = it
                        )
                    )
                },
                selectedTag = screenData.selected,
                categories = screenData.categories,
            )
        }
        items(
            items = screenData.animations,
            key = { it.metadata.hash },
        ) { animationsData ->
            AnimationCaptionCard(
                modifier = Modifier.padding(horizontal = 8.dp).width(300.dp).height(400.dp),
                animationData = animationsData,
                onClick = {
                    onDestination(
                        Destination.AnimationDetails(
                            animationsData.metadata.hash
                        )
                    )
                }
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChips(
    modifier: Modifier = Modifier,
    categories: List<SelectedQueryType.Tag>,
    onTagClicked: (SelectedQueryType) -> Unit,
    selectedTag: SelectedQueryType
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(
            items = categories,
            key = { it.keySearch }
        ) { categoryType ->
            FilterChip(
                modifier = Modifier,
                selected = categoryType == selectedTag,
                onClick = {
                    if (categoryType != selectedTag) {
                        onTagClicked(categoryType)
                    }else{
                        onTagClicked(SelectedQueryType.ExploreCategory.Explore)
                    }
                },
                label = {
                    Text(
                        text = categoryType.tag,
                        style =  MaterialTheme.typography.labelMedium,
                        fontWeight = if (categoryType == selectedTag) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    enabled = true,
                    selected = categoryType == selectedTag
                )
            )
        }
    }
}
