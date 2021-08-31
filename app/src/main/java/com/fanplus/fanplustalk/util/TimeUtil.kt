package com.fanplus.fanplustalk.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    fun getFormattedLongDay(time: Long): String? {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.getDefault())
        return dateFormat.format(Date(time))
    }

    fun getFormattedDay(time: Long): String? {
        val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        return dateFormat.format(Date(time))
    }

    fun getFormattedTime(time: Long): String? {
        val dateFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        return dateFormat.format(Date(time))
    }
}