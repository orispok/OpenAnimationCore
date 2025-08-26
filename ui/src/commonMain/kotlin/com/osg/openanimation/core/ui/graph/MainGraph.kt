package com.osg.openanimation.core.ui.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.osg.openanimation.core.ui.components.loading.LoadingAnimation
import com.osg.openanimation.core.ui.dashboard.AnimationUploadScreen
import com.osg.openanimation.core.ui.dashboard.DashboardView
import com.osg.openanimation.core.ui.dashboard.model.AnimationUploadScreenViewModel
import com.osg.openanimation.core.ui.dashboard.model.DashboardViewModel
import com.osg.openanimation.core.ui.details.AnimationDetailsPanes
import com.osg.openanimation.core.ui.details.model.AnimationDetailsViewModel
import com.osg.openanimation.core.ui.details.model.ds.DetailsScreenStates
import com.osg.openanimation.core.ui.home.model.AnimationViewModel
import com.osg.openanimation.core.ui.home.ui.HomePanel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable<Destination.Home> { backStackEntry ->
        val queryType = backStackEntry.toRoute<Destination.Home>().resolveQuery()
        val mainViewModel = koinViewModel<AnimationViewModel>(
            key = queryType.keySearch
        ) {
            parametersOf(queryType)
        }
        val uiState by mainViewModel.uiState.collectAsState()
        HomePanel(
            modifier = Modifier,
            uiData = uiState,
            onNavigation = { dst ->
                navController.navigate(dst)
            },
        )
    }

    composable<Destination.AnimationDetails> { backStackEntry ->
        val detailsViewModel = koinViewModel<AnimationDetailsViewModel> {
            parametersOf(backStackEntry.toRoute<Destination.AnimationDetails>().hash)
        }
        val uiState by detailsViewModel.uiState.collectAsState()
        when (val detailsUiState = uiState) {
            DetailsScreenStates.Loading -> {
                LoadingAnimation(
                    modifier = Modifier.Companion.fillMaxSize()
                )
            }

            is DetailsScreenStates.Success -> {
                AnimationDetailsPanes(
                    detailsUiState = detailsUiState,
                    onLikeClick = detailsViewModel::onLikeClick,
                    onDownloadClick = detailsViewModel::onDownloadClick,
                    onDismissSignInDialog = detailsViewModel::onDismissSignInDialog,
                    onTagClick = {
                        val selectedQueryType = SelectedQueryType.Tag(it)
                        navController.navigate(
                            Destination.Home(selectedQueryType)
                        )
                    },
                    onRelatedAnimationClicked = { animation ->
                        navController.navigate(
                            Destination.AnimationDetails(animation.metadata.hash)
                        )
                    },
                    onPalletSelect = detailsViewModel::onPalletSelect,
                )
            }
        }

    }

    composable<Dashboard> { backStackEntry ->
        val dashboardViewModel = koinViewModel<DashboardViewModel>()
        val uiState by dashboardViewModel.uiState.collectAsState()
        DashboardView(
            modifier = Modifier.Companion,
            dashboardUiState = uiState,
            onAnimationEdit = { animationId ->
                navController.navigate(
                    EditAnimation(animationId)
                )
            }
        )
    }

    composable<EditAnimation> { backStackEntry ->
        val arg = backStackEntry.toRoute<EditAnimation>()
        val viewModel = koinViewModel<AnimationUploadScreenViewModel>(
            key = arg.animationId
        ) {
            parametersOf(arg)
        }
        val uiState by viewModel.uiState.collectAsState()
        AnimationUploadScreen(
            modifier = Modifier.Companion,
            uiState = uiState,
            onPaletteSelected = viewModel::onPaletteSelected,
            onUploadClick = viewModel::onPublishClick,
            onTitleChanged = viewModel::onTitleChanged,
            onTagsChanged = viewModel::onTagsChanged,
            onRemovalClick = {
                viewModel.onRemovalClick {
                    navController.popBackStack()
                }
            },
            onSaveClick = viewModel::onSaveClick
        )
    }
}