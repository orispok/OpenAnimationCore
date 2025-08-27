package com.osg.openanimation.core.ui.di.domain

import com.osg.openanimation.core.ui.components.signin.SignInProvider

fun interface SignInProviderFactory{
    fun buildSignInProviders(): List<SignInProvider>
}