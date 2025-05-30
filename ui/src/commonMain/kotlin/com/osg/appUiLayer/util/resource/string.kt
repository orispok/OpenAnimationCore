package com.osg.appUiLayer.util.resource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

val StringResource.string: String
    @Stable
    @Composable
    get() = stringResource(this)