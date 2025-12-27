package com.osg.openanimation.core.ui.graph

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.osg.openanimation.core.ui.components.bar.OpenNavigationWrapper
import com.osg.openanimation.core.ui.components.bar.SearchAnimationBar
import com.osg.openanimation.core.ui.di.domain.AnimationMetadataRepository
import com.osg.openanimation.core.ui.di.domain.UserRepository
import com.osg.openanimation.core.ui.di.domain.UserSessionState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.asFlow
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
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
                    onNavigate = {
                        navController.navigate(it)
                    },
                    categories = animationCatalogState.map(SelectedQueryType::Tag).toImmutableList(),
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
                mainGraph(navController)
            }

            LaunchedEffect(navController) {
                onNavHostReady(navController)
            }
        }
    }
}

