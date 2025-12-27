package com.osg.openanimation.core.ui.components.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList


@Composable
fun SignInControllers(
    modifier: Modifier = Modifier,
    signInProviders: ImmutableList<SignInProvider>,
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