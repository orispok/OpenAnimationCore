package com.osg.openanimation.core.ui.di.domain

import androidx.compose.runtime.Immutable
import com.osg.openanimation.core.ui.components.signin.SignInProvider

@Immutable
fun interface SignInProviderFactory{
    fun buildSignInProviders(): List<SignInProvider>
}