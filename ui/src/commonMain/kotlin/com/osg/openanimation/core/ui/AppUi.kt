package com.osg.openanimation.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.osg.openanimation.core.ui.di.domain.AnimationContentLoader
import com.osg.openanimation.core.ui.di.domain.AnimationInitialUpload
import com.osg.openanimation.core.ui.di.domain.AnimationMetadataRepository
import com.osg.openanimation.core.ui.di.domain.AppLinkProvider
import com.osg.openanimation.core.ui.di.domain.ReportSubmissionService
import com.osg.openanimation.core.ui.di.domain.SignInProviderFactory
import com.osg.openanimation.core.ui.di.domain.UserRepository
import com.osg.openanimation.core.ui.di.model.viewModelModule
import com.osg.openanimation.core.ui.graph.AppGraph
import com.osg.openanimation.core.ui.theme.TrueTheme
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class BaseApp(
    metadataRepository: Definition<AnimationMetadataRepository>,
    userRepository: Definition<UserRepository>,
    dataFetcher: Definition<AnimationContentLoader>,
    reportHandlerLoader: Definition<ReportSubmissionService>,
    animationInitialUpload: Definition<AnimationInitialUpload>,
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
            single<AnimationInitialUpload>(
                definition = animationInitialUpload
            )
            single<AppLinkProvider> { AppLinkProvider(baseUrl) }
            singleOf<SignInProviderFactory>(signInLoader)
        }
        val allModules = platformModules + mediaModule + viewModelModule
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