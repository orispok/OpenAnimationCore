package com.osg.openanimation.core.ui.dashboard.model

import androidx.lifecycle.ViewModel
import com.osg.openanimation.core.ui.dashboard.state.DashboardUiState
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel: ViewModel() {
    val uiState: StateFlow<DashboardUiState> = TODO()

    fun onFileDropped(content: String) {
        // Handle file drop logic here
    }

    fun onAnimationClick(animationId: String) {
        // Handle animation click logic here
    }
}