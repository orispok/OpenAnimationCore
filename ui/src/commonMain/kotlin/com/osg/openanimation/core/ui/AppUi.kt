package com.osg.openanimation.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.osg.openanimation.core.ui.di.*
import com.osg.openanimation.core.ui.graph.AppGraph
import com.osg.openanimation.core.ui.theme.TrueTheme
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.ParametersHolder
import org.koin.core.scope.Scope
import org.koin.dsl.module

class BaseApp(
    metadataRepository: Scope.(ParametersHolder) -> AnimationMetadataRepository,
    userRepository: Scope.(ParametersHolder) -> UserRepository,
    dataFetcher: Scope.(ParametersHolder) -> AnimationContentLoader,
    reportHandlerLoader: Scope.(ParametersHolder) -> ReportSubmissionService,
    signInLoader: () -> SignInProviderFactory,
    baseUrl: String,
) {
    init {
        val mediaModule = module {
            single<AnimationMetadataRepository>(
                definition = metadataRepository
            )
            single<AnimationContentLoader>(
                definition = dataFetcher
            )
            single<UserRepository>(
                definition = userRepository
            )
            single<ReportSubmissionService>(
                definition = reportHandlerLoader
            )
            single<AppLinkProvider> { AppLinkProvider(baseUrl) }
            singleOf<SignInProviderFactory>(signInLoader)
        }
        startKoin {
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
            linkProvider = koinInject(),
            isDarkTheme = isDarkMode,
        ) {
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