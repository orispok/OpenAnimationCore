package com.osg.core.ui.components.signin

import androidx.compose.runtime.Composable

data class SignInResult(
    val uid: String,
    val name: String? = null,
    val email: String? = null,
    val accessToken: String? = null,
)


enum class SignInIdentifier {
    Google,
}

interface SignInProvider {
    val identifier: SignInIdentifier
    @Composable
    fun SignInDialog(
        onComplete: (Result<SignInResult>) -> Unit
    )
}