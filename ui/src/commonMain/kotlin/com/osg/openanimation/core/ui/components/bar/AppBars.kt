@file:OptIn(ExperimentalMaterial3Api::class)

package com.osg.openanimation.core.ui.components.bar

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialog
import com.osg.openanimation.core.ui.dashboard.JsonImportDialog
import com.osg.openanimation.core.ui.di.domain.UserSessionState
import com.osg.openanimation.core.ui.graph.Dashboard
import com.osg.openanimation.core.ui.graph.Destination
import com.osg.openanimation.core.ui.graph.EditAnimation
import com.osg.openanimation.core.ui.graph.SelectedQueryType
import com.osg.openanimation.core.ui.util.icons.Tag
import com.osg.openanimation.core.ui.util.icons.Upload
import com.osg.openanimation.core.ui.util.icons.brandingpack.LogoVector
import com.osg.openanimation.core.ui.util.icons.githubVector
import kotlinx.collections.immutable.ImmutableList

@Composable
fun OpenNavSuiteScope.SearchAnimationBar(
    userSessionState: UserSessionState,
    categories: ImmutableList<SelectedQueryType>,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onSignOutClick: () -> Unit,
    onNavigate: (Destination) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    AnimatedContent(
        targetState = isShowSearchField,
        label = "SearchAnimationBar",
        modifier = modifier
    ){ expanded ->
        if (expanded) {
            SearchBar(
                onSearchItemSelected = {
                    onNavigate(
                        Destination.Home(it)
                    )
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
                onNavigate = onNavigate,
            )
        }
    }
}

@Composable
fun OpenNavSuiteScope.RegularAppBar(
    userSessionState: UserSessionState,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onNavigate: (Destination) -> Unit,
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
                IconButton(onClick = {
                    onNavigate(
                        Destination.Home(SelectedQueryType.ExploreCategory.Trending)
                    )
                }) {
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
                        contentDescription = "search"
                    )
                }
            }

            UploadButtonBar(
                isSignedIn = userSessionState is UserSessionState.SignedIn,
                onNavigate = onNavigate
            )

            val uriHandler = LocalUriHandler.current
            IconButton(onClick = {
                uriHandler.openUri("https://github.com/orispok/OpenAnimationApp")
            }) {
                Icon(
                    imageVector = githubVector,
                    contentDescription = "github",
                )
            }

            when (userSessionState) {
                is UserSessionState.SignedIn -> {
                    UserProfileSignedInButton(
                        onLogoutClick = onSignOutClick,
                        onNavigateToProfile ={
                            onNavigate(
                                Destination.Account
                            )
                        },
                        onNavigateToDashboard = {
                            onNavigate(
                                Dashboard
                            )
                        },
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
fun UploadButtonBar(
    isSignedIn: Boolean,
    onNavigate: (Destination) -> Unit
){
    var openDialog by remember { mutableStateOf(false) }
    IconButton(onClick = {
        openDialog = true
    }) {
        Icon(
            imageVector = Icons.Filled.Upload,
            contentDescription = "Upload",
        )
    }

    if (openDialog){
        if (isSignedIn) {
            JsonImportDialog(
                openDialog = openDialog,
                onAnimationEdit = {
                    onNavigate(EditAnimation(it))
                }
            ){
                openDialog = false
            }
        }else{
            SignInReasoningDialog {
                openDialog = false
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearchItemSelected: (SelectedQueryType) -> Unit,
    categories: ImmutableList<SelectedQueryType>,
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