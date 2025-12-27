package com.osg.openanimation.core.ui.components.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

data class SignInResult(
    val uid: String,
    val name: String? = null,
    val email: String? = null,
    val accessToken: String? = null,
)


enum class SignInIdentifier {
    Google,
}

@Immutable
interface SignInProvider {
    val identifier: SignInIdentifier
    @Composable
    fun SignInDialog(
        onComplete: (Result<SignInResult>) -> Unit
    )
}