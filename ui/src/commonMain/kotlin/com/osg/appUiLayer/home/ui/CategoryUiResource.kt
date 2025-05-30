package com.osg.appUiLayer.home.ui

import openanimation.appuilayer.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.osg.previewopenanimation.core.AnimationCategory

val AnimationCategory.strResource: StringResource
    get() = when(this){
        AnimationCategory.MINIMAL -> Res.string.minimal
        AnimationCategory.ABSTRACT -> Res.string.abstract
        AnimationCategory.THREE_D_STYLE -> Res.string.three_d_style
        AnimationCategory.FLAT_DESIGN -> Res.string.flat_design
        AnimationCategory.HAND_DRAWN -> Res.string.hand_drawn
        AnimationCategory.NEON -> Res.string.neon
        AnimationCategory.LINE_ART -> Res.string.line_art
        AnimationCategory.GRADIENT_GLOW -> Res.string.gradient_glow
        AnimationCategory.ONBOARDING -> Res.string.onboarding
        AnimationCategory.LOADING -> Res.string.loading
        AnimationCategory.SUCCESS -> Res.string.success
        AnimationCategory.ERROR -> Res.string.error
        AnimationCategory.EMPTY_STATES -> Res.string.empty_states
        AnimationCategory.NAVIGATION -> Res.string.navigation
        AnimationCategory.TRANSITIONS -> Res.string.transitions
        AnimationCategory.TUTORIALS -> Res.string.tutorials
        AnimationCategory.HAPPY -> Res.string.happy
        AnimationCategory.SAD -> Res.string.sad
        AnimationCategory.SURPRISED -> Res.string.surprised
        AnimationCategory.CONFUSED -> Res.string.confused
        AnimationCategory.THUMBS_UP -> Res.string.thumbs_up
        AnimationCategory.CLAPPING -> Res.string.clapping
        AnimationCategory.LOVE -> Res.string.love
        AnimationCategory.CELEBRATION -> Res.string.celebration
        AnimationCategory.BUTTONS -> Res.string.buttons
        AnimationCategory.SWITCHES -> Res.string.switches
        AnimationCategory.TOGGLES -> Res.string.toggles
        AnimationCategory.ICONS -> Res.string.icons
        AnimationCategory.CHECKBOXES -> Res.string.checkboxes
        AnimationCategory.SLIDERS -> Res.string.sliders
        AnimationCategory.TOOLTIPS -> Res.string.tooltips
        AnimationCategory.ALERTS -> Res.string.alerts
        AnimationCategory.HOLIDAYS -> Res.string.holidays
        AnimationCategory.HALLOWEEN -> Res.string.halloween
        AnimationCategory.CHRISTMAS -> Res.string.christmas
        AnimationCategory.NEW_YEAR -> Res.string.new_year
        AnimationCategory.VALENTINES -> Res.string.valentines
        AnimationCategory.SUMMER -> Res.string.summer
        AnimationCategory.WINTER -> Res.string.winter
        AnimationCategory.BUSINESS -> Res.string.business
        AnimationCategory.EDUCATION -> Res.string.education
        AnimationCategory.FINANCE -> Res.string.finance
        AnimationCategory.HEALTHCARE -> Res.string.healthcare
        AnimationCategory.TECH -> Res.string.tech
        AnimationCategory.GAMING -> Res.string.gaming
        AnimationCategory.TRAVEL -> Res.string.travel
        AnimationCategory.ECOMMERCE -> Res.string.ecommerce
        AnimationCategory.PROGRESS -> Res.string.progress
        AnimationCategory.NOTIFICATIONS -> Res.string.notifications
        AnimationCategory.UPLOAD_DOWNLOAD -> Res.string.upload_download
        AnimationCategory.SYNC -> Res.string.sync
        AnimationCategory.SEARCH -> Res.string.search
        AnimationCategory.REFRESH -> Res.string.refresh
        AnimationCategory.SETTINGS -> Res.string.settings
        AnimationCategory.UNLOCK -> Res.string.unlock
        AnimationCategory.UNKNOWN -> Res.string.unknown
    }