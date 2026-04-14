package com.osg.openanimation.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.osg.openanimation.core.ui.graph.AppNavigation
import com.osg.openanimation.core.ui.theme.TrueTheme
import org.koin.compose.koinInject
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration

@org.koin.core.annotation.Module
@ComponentScan
@Configuration
class SharedModule

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