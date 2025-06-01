package com.osg.core.ui.details

import com.osg.openanimation.core.ui.details.toHumanReadableSize
import kotlin.test.Test
import kotlin.test.assertEquals

class HumanReadableSizeTest {

    @Test
    fun testToHumanReadableSize() {
        // Bytes
        assertEquals("0 Bytes", 0L.toHumanReadableSize())
        assertEquals("100 Bytes", 100L.toHumanReadableSize())
        assertEquals("1023 Bytes", 1023L.toHumanReadableSize())

        // Kilobytes
        assertEquals("1 KB", 1024L.toHumanReadableSize())
        assertEquals("1.5 KB", (1024L + 512L).toHumanReadableSize())
        assertEquals("2 KB", (2048L).toHumanReadableSize())

        // Megabytes
        assertEquals("1 MB", (1024L * 1024L).toHumanReadableSize())
        assertEquals("1.5 MB", (1024L * 1024L + 512L * 1024L).toHumanReadableSize())
        assertEquals("2 MB", (2L * 1024L * 1024L).toHumanReadableSize())

        // Gigabytes
        assertEquals("1 GB", (1024L * 1024L * 1024L).toHumanReadableSize())
        assertEquals("1.5 GB", (1024L * 1024L * 1024L + 512L * 1024L * 1024L).toHumanReadableSize())
        assertEquals("2 GB", (2L * 1024L * 1024L * 1024L).toHumanReadableSize())
    }
}