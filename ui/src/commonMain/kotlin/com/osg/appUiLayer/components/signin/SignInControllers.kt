package com.osg.appUiLayer.components.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun SignInControllers(
    modifier: Modifier = Modifier,
    signInProviders: List<SignInProvider>,
    onComplete: (Result<SignInResult>) -> Unit = {}
) {

    var signInDialogs by remember {
        mutableStateOf<SignInProvider?>(null)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        signInProviders.forEach { provider ->
            BrandedButton(
                modifier = Modifier
                    .widthIn(max = 280.dp),
                brandedButtonType = provider.identifier.brandedButtonType,
                onClick = {
                    signInDialogs = provider
                }
            )
        }
    }
    signInDialogs?.SignInDialog(
        onComplete = onComplete
    )
}