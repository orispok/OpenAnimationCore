package com.osg.core.ui.components.license

import com.osg.openanimation.core.data.report.LicenseType

val LicenseType.description: String
    get() = when (this) {
        LicenseType.UNKNOWN -> "Not Verified for Commercial Use - Personal Use or Inspiration Only"
        LicenseType.MIT -> "free to use, modify, and distribute with attribution."
    }