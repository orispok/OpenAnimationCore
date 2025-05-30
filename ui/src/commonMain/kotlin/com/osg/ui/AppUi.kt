package com.osg.appUiLayer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.osg.appUiLayer.di.AnimationDataFetcher
import com.osg.appUiLayer.di.AnimationMetadataRepository
import com.osg.appUiLayer.di.AppLinkProvider
import com.osg.appUiLayer.di.SignInProviderFactory
import com.osg.appUiLayer.graph.AppGraph
import com.osg.appUiLayer.theme.TrueTheme
import com.osg.appUiLayer.user.UserRepository
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.ParametersHolder
import org.koin.core.scope.Scope
import org.koin.dsl.module

class BaseApp(
    metadataRepository: Scope.(ParametersHolder) -> AnimationMetadataRepository,
    userRepository: Scope.(ParametersHolder) -> UserRepository,
    dataFetcher: Scope.(ParametersHolder) -> AnimationDataFetcher,
    signInLoader: () -> SignInProviderFactory,
    baseUrl: String = "http://localhost:8080",
    ){
    init {
        val mediaModule =  module {
            single<AnimationMetadataRepository>(
                definition = metadataRepository
            )
            single<AnimationDataFetcher>(
                definition = dataFetcher
            )
            single<UserRepository>(
                definition = userRepository
            )
            single<AppLinkProvider> { AppLinkProvider(baseUrl) }
            singleOf<SignInProviderFactory>(signInLoader)
        }
        startKoin{
            modules(mediaModule)
        }
    }

    @Composable
    fun AppEntry(
        onNavHostReady: suspend (NavController) -> Unit = {}
    ) {
        val systemDark = isSystemInDarkTheme()
        var isDarkMode by remember { mutableStateOf(systemDark) }
        TrueTheme(
            isDarkTheme = isDarkMode,
        ){
            AppGraph(
                isDarkMode = isDarkMode,
                onSwitchMode = {
                    isDarkMode = it
                },
                onNavHostReady = onNavHostReady
            )
        }
    }
}