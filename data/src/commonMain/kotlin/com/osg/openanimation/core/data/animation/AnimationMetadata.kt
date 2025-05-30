package com.osg.openanimation.core.data.animation

import com.osg.openanimation.core.data.report.LicenseType
import kotlinx.serialization.Serializable

@Serializable
data class AnimationMetadata(
    val name: String,
    val localFileName: String = "",
    val author: String? = null,
    val sourcePath: String = "unknown",
    val hash: String,
    val sizeBytes: Long = 256 * 1024L,
    val description: String = "",
    val uploadedDate: Long = 1747044716598,
    val license: LicenseType = LicenseType.UNKNOWN,
    val category: AnimationCategory = AnimationCategory.UNKNOWN,
    val tags: Set<String> = emptySet(),
)
@Serializable
data class StorageInfo(
    val count: Int,
    val animations: List<AnimationMetadata>
)