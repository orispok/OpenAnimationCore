package com.osg.openanimation.core.ui.di.model

import com.osg.openanimation.core.ui.dashboard.model.DashboardViewModel
import com.osg.openanimation.core.ui.details.model.AnimationDetailsViewModel

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (animationHash: String) ->
        AnimationDetailsViewModel(
            animationHash = animationHash,
            get(),
            get(),
            get()
        )
    }
    viewModelOf(::DashboardViewModel)
}
