package com.osg.openanimation.core.ui

import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.osg.openanimation.core.ui.di.*
import com.osg.openanimation.core.ui.graph.AppGraph
import com.osg.openanimation.core.ui.theme.TrueTheme
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
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
    platformModules : List<Module> = emptyList()
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
        val allModules = platformModules + mediaModule
        startKoin {
            modules(allModules)
        }
    }

    @Composable
    fun AppEntry(
        onNavHostReady: suspend (NavController) -> Unit = {}
    ) {
        var isDarkTheme by remember { mutableStateOf(true) }
        TrueTheme(
            linkProvider = koinInject(),
            isDarkTheme = isDarkTheme,
        ) {
            AppGraph(
                isDarkTheme = isDarkTheme,
                onSwitchMode = {
                    isDarkTheme = it
                },
                onNavHostReady = onNavHostReady
            )
        }
    }
}