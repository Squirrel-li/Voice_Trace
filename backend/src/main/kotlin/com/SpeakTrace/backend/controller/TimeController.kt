package com.SpeakTrace.backend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
class TimeController {

    @GetMapping("/api/time")
    fun currentTime(): Map<String, String> =
        mapOf("now" to ZonedDateTime.now(TimeZone.getDefault().toZoneId())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
}