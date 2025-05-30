package com.osg.appUiLayer.di

import com.osg.appUiLayer.components.signin.SignInProvider

fun interface SignInProviderFactory{
    fun buildSignInProviders(): List<SignInProvider>
}