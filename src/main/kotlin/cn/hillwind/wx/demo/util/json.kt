package cn.hillwind.wx.demo.util

import com.google.gson.GsonBuilder

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