package com.osg.openanimation.core.ui.util.time

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant


fun Long.millisToUtcDateTime(): LocalDateTime {
        return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC)
    }


fun toDefaultFormat(year: Int, month: Int, day: Int): String {
    return "$year-$month-$day"
}

fun Long.fromEpochToDayDateFormat(): String {
    val birthDate = this.millisToUtcDateTime()
    return toDefaultFormat(birthDate.year, birthDate.month.number, birthDate.day)
}
