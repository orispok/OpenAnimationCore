package com.osg.openanimation.core.ui.components.bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialog
import com.osg.openanimation.core.ui.generated.resources.Res
import com.osg.openanimation.core.ui.generated.resources.logout
import com.osg.openanimation.core.ui.util.icons.Logout

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
        SignInReasoningDialog {
            openSignInDialog = false
        }
    }
}