package com.osg.openanimation.core.ui.components.bar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.osg.openanimation.core.ui.di.LocalLinkProvider
import com.osg.core.di.data.SelectedQueryType
import com.osg.openanimation.core.ui.components.button.buttonUi
import com.osg.openanimation.core.ui.graph.Destination
import com.osg.openanimation.core.ui.theme.component.DarkLightSwitch
import com.osg.openanimation.core.ui.util.adaptive.ScreenSizeClass
import com.osg.openanimation.core.ui.util.adaptive.screenWidthClass
import com.osg.openanimation.core.ui.util.icons.MenuOpen
import com.osg.openanimation.core.ui.util.icons.PrivacyTip
import com.osg.openanimation.core.ui.util.icons.RoomService
import com.osg.openanimation.core.ui.util.resource.string
import kotlinx.coroutines.launch

sealed interface AppNavType {
    data object NavigationRail : AppNavType
    data object NavigationDrawer : AppNavType
}

data class OpenNavSuiteScope(
    val navSuiteType: AppNavType,
    val isShowSearchField: Boolean,
    val onDrawerClicked: () -> Unit,
    val onToggleSearch: (Boolean) -> Unit,
){
    val isNavDrawer: Boolean
        get() = navSuiteType == AppNavType.NavigationDrawer
}


@Composable
fun OpenNavigationWrapper(
    currentDestination: Destination?,
    isDarkMode: Boolean = false,
    onSwitchMode: (Boolean) -> Unit = {},
    onRailDst: (SelectedQueryType.ExploreCategory) -> Unit,
    content: @Composable OpenNavSuiteScope.() -> Unit
) {
    var isShowSearchField by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    // Avoid opening the modal drawer when there is a permanent drawer or a bottom nav bar,
    // but always allow closing an open drawer.

    val navLayoutType = when(screenWidthClass){
        ScreenSizeClass.COMPACT -> AppNavType.NavigationDrawer
        ScreenSizeClass.MEDIUM -> AppNavType.NavigationRail
        ScreenSizeClass.EXPANDED -> AppNavType.NavigationRail
    }

    val openNavSuiteScope = OpenNavSuiteScope(
        isShowSearchField = isShowSearchField,
        onToggleSearch = {
            isShowSearchField = it
        },
        navSuiteType = navLayoutType,
        onDrawerClicked = {
            coroutineScope.launch {
                when (drawerState.isOpen) {
                    true -> drawerState.close()
                    false -> drawerState.open()
                }
            }
        }
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalNavigationDrawerContent(
                currentDestination = currentDestination,
                isDarkMode = isDarkMode,
                onSwitchMode = onSwitchMode,
                onRailDst = onRailDst,
                onDrawerClicked = openNavSuiteScope.onDrawerClicked
            )
        },
        content = {
            if(openNavSuiteScope.isNavDrawer){
                openNavSuiteScope.content()
            }else{
                Row {
                    AppNavigationRail(
                        currentDestination = currentDestination,
                        onDrawerClicked = openNavSuiteScope.onDrawerClicked,
                        onRailDst = onRailDst,
                        onSearchClicked = {
                            isShowSearchField = !isShowSearchField
                        }
                    )
                    openNavSuiteScope.content()
                }
            }
        }
    )
}

fun Destination?.toQueryType(): SelectedQueryType? {
    return when (this) {
        is Destination.Home -> resolveQuery()
        is Destination.AnimationDetails -> null
        else -> null
    }
}

@Composable
fun AppNavigationRail(
    currentDestination: Destination?,
    onDrawerClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onRailDst: (SelectedQueryType.ExploreCategory) -> Unit,
) {
    NavigationRail(
        header = {
            IconButton(onClick = onDrawerClicked) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = ""
                )
            }
            FloatingActionButton(
                onClick = onSearchClicked,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    ) {
        SelectedQueryType.ExploreCategory.entries.forEach { entry ->
            val uiData = entry.buttonUi
            val isSelected = currentDestination.toQueryType() == entry
            NavigationRailItem(
                selected = isSelected,
                label = {
                    Text(
                        text = uiData.stringResource.string,
                    )
                },
                icon = {
                    Icon(
                        imageVector = uiData.resolveOnSelected(isSelected),
                        contentDescription = ""
                    )
                },
                onClick = {
                    onRailDst(entry)
                }
            )
        }
    }
}


@Composable
fun ModalNavigationDrawerContent(
    currentDestination: Destination?,
    isDarkMode: Boolean,
    onSwitchMode: (Boolean) -> Unit = {},
    onDrawerClicked: () -> Unit = {},
    onRailDst: (SelectedQueryType.ExploreCategory) -> Unit,
) {
    ModalDrawerSheet {
        IconButton(onClick = onDrawerClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                contentDescription = "close drawer",
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        SelectedQueryType.ExploreCategory.entries.forEach { entry ->
            val uiData = entry.buttonUi
            val isSelected = currentDestination.toQueryType() == entry
            NavigationDrawerItem(
                selected = isSelected,
                label = {
                    Text(
                        text = uiData.stringResource.string,
                    )
                },
                icon = {
                    Icon(
                        imageVector = uiData.resolveOnSelected(isSelected),
                        contentDescription = ""
                    )
                },
                onClick = {
                    onRailDst(entry)
                    onDrawerClicked()
                },
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        val height = 64.dp
        DarkLightSwitch(
            modifier = Modifier
                .padding(start = 6.dp)
                .height(height)
                .width(2*height).fillMaxWidth(),
            isDarkMode = isDarkMode,
            onSwitch = onSwitchMode
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        val uriHandler = LocalUriHandler.current
        val linkProvider = LocalLinkProvider.current
        NavigationDrawerItem(
            label = { Text("Terms of Service") },
            selected = false,
            icon = { Icon(Icons.Filled.RoomService, contentDescription = null) },
            onClick = {
                uriHandler.openUri(linkProvider.termUrl)
            }
        )
        NavigationDrawerItem(
            label = { Text("Privacy Policy") },
            selected = false,
            icon = { Icon(Icons.Filled.PrivacyTip, contentDescription = null) },
            onClick = {
                uriHandler.openUri(linkProvider.privacyUrl)
            }
        )
    }
}