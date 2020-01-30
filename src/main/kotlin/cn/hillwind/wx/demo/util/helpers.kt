package cn.hillwind.wx.demo.util

import com.google.gson.GsonBuilder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object JsonHelper {
    val gson = GsonBuilder().create()

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }
}

fun Any?.debug() {
    if (this != null) println(this)
}

fun Any?.toJson() = this?.let { JsonHelper.toJson(this) }

fun LocalDate.toDate(): Date {
    val zone = ZoneId.systemDefault()
    val instant: Instant = this.atStartOfDay().atZone(zone).toInstant()
    return Date.from(instant)
}