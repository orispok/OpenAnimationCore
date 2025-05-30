package com.osg.openanimation.core.ui.di

import com.osg.openanimation.core.ui.components.signin.SignInProvider

fun interface SignInProviderFactory{
    fun buildSignInProviders(): List<SignInProvider>
}