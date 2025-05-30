@file:OptIn(ExperimentalMaterial3Api::class)

package com.osg.openanimation.core.ui.components.bar

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialog

import com.osg.core.di.data.SelectedQueryType
import com.osg.openanimation.core.ui.di.UserSessionState
import com.osg.openanimation.core.ui.util.icons.Logout
import com.osg.openanimation.core.ui.util.icons.Tag
import com.osg.openanimation.core.ui.util.icons.brandingpack.LogoVector
import com.osg.openanimation.core.ui.util.icons.githubVector
import openanimationapp.core.ui.generated.resources.Res
import openanimationapp.core.ui.generated.resources.logout

@Composable
fun OpenNavSuiteScope.SearchAnimationBar(
    userSessionState: UserSessionState,
    categories: List<SelectedQueryType>,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onSignOutClick: () -> Unit,
    onSearchItemSelected: (SelectedQueryType) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    AnimatedContent(
        targetState = isShowSearchField,
        label = "SearchAnimationBar",
        modifier = modifier
    ){ expanded ->
        if (expanded) {
            AppSearchController(
                onSearchItemSelected = {
                    onSearchItemSelected(it)
                    onToggleSearch(false)
                },
                onQueryChange = { query = it },
                categories = categories,
                query = query,
                expanded = expanded,
                onExpandedChange = onToggleSearch,
                modifier = modifier.statusBarsPadding().fillMaxWidth(),
                focusRequester = focusRequester
            )
        } else {
            RegularAppBar(
                topAppBarScrollBehavior = topAppBarScrollBehavior,
                modifier = modifier,
                userSessionState = userSessionState,
                onSignOutClick = onSignOutClick,
                onSearchClick = {
                    onToggleSearch(isShowSearchField.not())
                },
                onLogoClick = {
                    onSearchItemSelected(SelectedQueryType.ExploreCategory.Explore)
                },
            )
        }
    }
}

@Composable
fun OpenNavSuiteScope.RegularAppBar(
    userSessionState: UserSessionState,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onLogoClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            Row {
                if (isNavDrawer){
                    IconButton(onClick = onDrawerClicked) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = onLogoClick) {
                    Icon(
                        imageVector = LogoVector,
                        contentDescription = "Localized description",
                        tint = Color.Unspecified,
                    )
                }
            }
        },
        title = {},
        actions = {
            if (isNavDrawer) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Localized description"
                    )
                }
            }
            val uriHandler = LocalUriHandler.current
            IconButton(onClick = {
                uriHandler.openUri("https://github.com/orispok/OpenAnimation")
            }) {
                Icon(
                    imageVector = githubVector,
                    contentDescription = "Localized description"
                )
            }

            when (userSessionState) {
                is UserSessionState.SignedIn -> {
                    UserProfileSignedInButton(
                        onLogoutClick = onSignOutClick
                    )
                }
                UserSessionState.SignedOut -> {
                    UserProfileSignedOutButton()
                }
            }
        },
        scrollBehavior = topAppBarScrollBehavior,
        modifier = modifier
    )
}

@Composable
fun UserProfileSignedInButton(
    onLogoutClick: () -> Unit = {}
){
    OptionsButton(
        imageVector = Icons.Filled.AccountCircle,
        dropDownOptions = listOf(
            GeneralIconButtonItem(
                stringResource = Res.string.logout,
                imageVector = Icons.AutoMirrored.Filled.Logout,
                onClick = onLogoutClick
            )
        ),
    )
}

@Composable
fun UserProfileSignedOutButton(){
    var openSignInDialog by remember { mutableStateOf(false) }
    TextButton(onClick = {
        openSignInDialog = true
    }) {
        Text(
            text = "Sign In",
            color = MaterialTheme.colorScheme.primary
        )
    }
    if (openSignInDialog) {
        SignInReasoningDialog{
            openSignInDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchController(
    onSearchItemSelected: (SelectedQueryType) -> Unit,
    categories: List<SelectedQueryType>,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    val searchResults = remember { mutableStateListOf<SelectedQueryType>() }
    LaunchedEffect(query) {
        searchResults.clear()
        if (query.isNotEmpty()) {
            searchResults.addAll(
                categories.filter {
                    it.keySearch.startsWith(
                        prefix = query,
                        ignoreCase = true
                    )
                }.take(7)
            )
        }
    }
    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    onSearchItemSelected(
                        SelectedQueryType.FreeText(
                            text = query
                        )
                    )
                },
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                modifier = Modifier.focusRequester(focusRequester),
                placeholder = { Text(text = "Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable {
                                onQueryChange("")
                                onExpandedChange(false)
                            },
                    )
                },
            )
        },
        expanded = expanded && query.isNotEmpty(),
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        content = {
            LazyColumn(
                modifier = Modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    if (query.isNotEmpty()) {
                        ListItem(
                            headlineContent = {
                                Text("search for $query")
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "query",
                                )
                            },
                            modifier = Modifier.clickable {
                                onSearchItemSelected(
                                    SelectedQueryType.FreeText(
                                        text = query
                                    )
                                )
                            }
                        )
                    }
                }
                items(items = searchResults, key = { it.keySearch }) { categoryType ->
                    val text = when (categoryType) {
                        is SelectedQueryType.Tag -> categoryType.tag
                        else -> "All"
                    }
                    ListItem(
                        headlineContent = { Text(text) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = "Tag",
                            )
                        },
                        modifier = Modifier.clickable {
                            onSearchItemSelected(categoryType)
                        }
                    )
                }
            }
        }
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}