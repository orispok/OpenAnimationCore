package org.osg.previewopenanimation.core.data.stats

import kotlinx.serialization.Serializable

@Serializable
data class DownloadRecord(
    val timestamp: Long,
)

@Serializable
data class DownloadStatistics(
    val downloadRecord: Map<String, DownloadRecord>,
)