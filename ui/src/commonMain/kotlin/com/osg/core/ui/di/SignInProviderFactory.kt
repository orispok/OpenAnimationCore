package com.osg.core.ui.di

import com.osg.core.ui.components.signin.SignInProvider

fun interface SignInProviderFactory{
    fun buildSignInProviders(): List<SignInProvider>
}