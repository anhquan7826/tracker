package com.anhquan.tracker_client.utils

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getTimeString(epoch: Long, format: String = "HH:mm"): String {
    val instant = OffsetDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.of("GMT+7"))
    val formatter = DateTimeFormatter.ofPattern(format)
    return instant.format(formatter)
}

fun getHour(epoch: Long): Int {
    return OffsetDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.of("GMT+7")).hour
}

fun now(): Long {
    return Instant.now().atZone(ZoneId.of("GMT+7")).toEpochSecond()
}