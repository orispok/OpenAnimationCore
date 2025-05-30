package com.osg.appUiLayer.components.license

import org.osg.previewopenanimation.core.LicenseType

val LicenseType.description: String
    get() = when (this) {
        LicenseType.UNKNOWN -> "Not Verified for Commercial Use - Personal Use or Inspiration Only"
        LicenseType.MIT -> "free to use, modify, and distribute with attribution."
    }