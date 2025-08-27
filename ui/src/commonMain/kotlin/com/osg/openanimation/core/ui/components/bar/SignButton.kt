package com.osg.openanimation.core.ui.components.bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialog
import com.osg.openanimation.core.ui.generated.resources.Res
import com.osg.openanimation.core.ui.generated.resources.dashboard
import com.osg.openanimation.core.ui.generated.resources.*
import com.osg.openanimation.core.ui.util.icons.Logout
import com.osg.openanimation.core.ui.util.icons.Workspaces

@Composable
fun UserProfileSignedInButton(
    onNavigateToProfile: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onLogoutClick: () -> Unit
){
    OptionsButton(
        imageVector = Icons.Filled.AccountCircle,
        dropDownOptions = listOf(
            GeneralIconButtonItem(
                stringResource = Res.string.account,
                imageVector = Icons.Default.AccountCircle,
                onClick = onNavigateToProfile
            ),
            GeneralIconButtonItem(
                stringResource = Res.string.dashboard,
                imageVector = Icons.Filled.Workspaces,
                onClick = onNavigateToDashboard
            ),
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
        SignInReasoningDialog {
            openSignInDialog = false
        }
    }
}