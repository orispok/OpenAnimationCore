package com.osg.openanimation.core.ui.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.osg.openanimation.core.ui.components.bar.OpenNavigationWrapper
import com.osg.openanimation.core.ui.components.bar.SearchAnimationBar
import com.osg.openanimation.core.ui.components.loading.LoadingAnimation
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialogView
import com.osg.openanimation.core.ui.dashboard.AnimationUploadScreen
import com.osg.openanimation.core.ui.dashboard.DashboardView
import com.osg.openanimation.core.ui.dashboard.model.AnimationUploadScreenViewModel
import com.osg.openanimation.core.ui.dashboard.model.DashboardViewModel
import com.osg.openanimation.core.ui.details.AnimationDetailsPanes
import com.osg.openanimation.core.ui.details.model.AnimationDetailsViewModel
import com.osg.openanimation.core.ui.details.model.ds.DetailsScreenStates
import com.osg.openanimation.core.ui.di.domain.AnimationMetadataRepository
import com.osg.openanimation.core.ui.di.domain.UserRepository
import com.osg.openanimation.core.ui.di.domain.UserSessionState
import com.osg.openanimation.core.ui.home.domain.ExploreScreenStates
import com.osg.openanimation.core.ui.home.model.AnimationViewModel
import com.osg.openanimation.core.ui.home.ui.AnimationGrid
import kotlinx.coroutines.flow.asFlow
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppGraph(
    isDarkTheme: Boolean = false,
    onSwitchMode: (Boolean) -> Unit = {},
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.currentGraph
    OpenNavigationWrapper(
        isDarkMode = isDarkTheme,
        onSwitchMode = onSwitchMode,
        currentDestination = currentDestination,
        onRailDst = {
            when (it) {
                Dashboard -> {
                    navController.navigate(Dashboard)
                }
                is SelectedQueryType -> {
                    navController.navigate(Destination.Home(it))
                }
            }

        },
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                val userRepository = koinInject<UserRepository>()
                val animationRepo = koinInject<AnimationMetadataRepository>()
                val animationCatalogState by (animationRepo::fetchTags.asFlow()).collectAsState(
                    emptySet()
                )
                val userSessionState by userRepository.profileFlow.collectAsState(UserSessionState.SignedOut)
                SearchAnimationBar(
                    onSearchItemSelected = {
                        navController.navigate(
                            Destination.Home(it)
                        )
                    },
                    categories = animationCatalogState.map(SelectedQueryType::Tag),
                    modifier = Modifier,
                    topAppBarScrollBehavior = scrollBehavior,
                    userSessionState = userSessionState,
                    onSignOutClick = userRepository::onUserSignOut,
                )
            },
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = Destination.Home(),
            ) {
                composable<Destination.Home> { backStackEntry ->
                    val queryType = backStackEntry.toRoute<Destination.Home>().resolveQuery()
                    val mainViewModel = viewModel {
                        AnimationViewModel(
                            initialQueryType = queryType
                        )
                    }
                    val uiState by mainViewModel.uiState.collectAsState()
                    when (val uiData = uiState) {
                        is ExploreScreenStates.Success -> {
                            AnimationGrid(
                                modifier = Modifier,
                                screenData = uiData,
                                onDestination = { dst ->
                                    navController.navigate(
                                        dst
                                    )
                                }
                            )
                        }

                        ExploreScreenStates.RequiredLogin -> {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                SignInReasoningDialogView(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                )
                            }
                        }

                        ExploreScreenStates.Loading -> {
                            LoadingAnimation(
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                composable<Destination.AnimationDetails> { backStackEntry ->
                    val animationHash = backStackEntry.toRoute<Destination.AnimationDetails>().hash
                    val detailsViewModel = koinViewModel<AnimationDetailsViewModel> {
                        parametersOf(animationHash)
                    }
                    val uiState by detailsViewModel.uiState.collectAsState()
                    when (val detailsUiState = uiState) {
                        DetailsScreenStates.Loading -> {
                            LoadingAnimation(
                                modifier = Modifier.fillMaxSize()
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
                        modifier = Modifier.padding(innerPadding),
                        dashboardUiState = uiState,
                        onAnimationEdit = { animationId ->
                            navController.navigate(
                                EditAnimation(animationId)
                            )
                        }
                    )
                }

                composable<EditAnimation> { backStackEntry ->
                    val viewModel = koinViewModel<AnimationUploadScreenViewModel>{
                        parametersOf(backStackEntry.toRoute<EditAnimation>())
                    }
                    val uiState by viewModel.uiState.collectAsState()
                    AnimationUploadScreen(
                        modifier = Modifier.padding(innerPadding),
                        uiState = uiState,
                        onPaletteSelected = viewModel::onPaletteSelected,
                        onUploadClick = viewModel::onUploadClick,
                        onTitleChanged = viewModel::onTitleChanged,
                        onTagsChanged = viewModel::onTagsChanged
                    )
                }

            }

            LaunchedEffect(navController) {
                onNavHostReady(navController)
            }
        }
    }
}