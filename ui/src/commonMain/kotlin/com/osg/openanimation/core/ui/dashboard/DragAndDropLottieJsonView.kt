package com.osg.openanimation.core.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.ui.dashboard.dragAndDrop.resolveDroppedContent
import com.osg.openanimation.core.ui.di.domain.AnimationInitialUpload
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@Composable
fun DragAndDropLottieJsonViewManger(
    modifier: Modifier = Modifier,
    animationInitialUpload: AnimationInitialUpload = koinInject(),
    onNavigateToDetails: (String) -> Unit,
) {
    var state by remember { mutableStateOf<DragAndDropState>(DragAndDropState.Initial) }
    val scope = rememberCoroutineScope()
    DragAndDropLottieJsonView(
        modifier = modifier,
        state = state,
        onProcessing = {
            state = DragAndDropState.Processing
        },
        onFileDropped = { content ->
            scope.launch {
                if (content == null) {
                    state = DragAndDropState.Initial
                }else{
                    val result = animationInitialUpload.processUploadAnimation(content)
                    onNavigateToDetails(result)
                }
            }
        }
    )
}
@Composable
fun DragAndDropLottieJsonView(
    modifier: Modifier = Modifier,
    state: DragAndDropState,
    onProcessing: () -> Unit,
    onFileDropped: (String?) -> Unit,
) {
    var isHovered by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onEntered(event: DragAndDropEvent) {
                super.onEntered(event)
                isHovered = true
            }

            override fun onExited(event: DragAndDropEvent) {
                super.onExited(event)
                isHovered = false
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                onProcessing()
                scope.launch {
                    val resolved = resolveDroppedContent(event)
                    onFileDropped(resolved)
                }
                return true
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Drag and Drop Lottie JSON",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        brush = SolidColor(
                            when {
                                isHovered -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            }
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { true },
                    target = dragAndDropTarget
                ),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is DragAndDropState.Initial -> {
                    Text(
                        text = "Drop your Lottie JSON file here",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }


                is DragAndDropState.Processing -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Processing...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
