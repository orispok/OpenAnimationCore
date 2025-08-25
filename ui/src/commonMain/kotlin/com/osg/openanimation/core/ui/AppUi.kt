package com.osg.openanimation.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.osg.openanimation.core.ui.di.domain.AppLinkProvider
import com.osg.openanimation.core.ui.graph.AppNavigation
import com.osg.openanimation.core.ui.theme.TrueTheme
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

class BaseApp(
    baseUrl: String,
    platformModules : List<Module> = emptyList()
) {
    init {
        val mediaModule = module {
            single<AppLinkProvider> { AppLinkProvider(baseUrl) }
        }
        val allModules = platformModules + mediaModule + defaultModule
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
            AppNavigation(
                isDarkTheme = isDarkTheme,
                onSwitchMode = {
                    isDarkTheme = it
                },
                onNavHostReady = onNavHostReady
            )
        }
    }
}