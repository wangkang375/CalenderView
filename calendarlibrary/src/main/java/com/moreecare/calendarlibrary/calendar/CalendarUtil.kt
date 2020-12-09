package com.moreecare.moreeui.calendar

import android.content.Context
import android.util.Log
import java.util.*
import java.util.logging.Logger


//获取这个月多少天
fun Calendar.getMaxDay(): Int {
    return this.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun Calendar.getYear(): Int {
    return this.get(Calendar.YEAR)
}

fun Calendar.getMonth(): Int {
    return this.get(Calendar.MONTH)
}

fun Calendar.getDay(): Int {
    return this.get(Calendar.DAY_OF_MONTH)
}

fun String.logDmoth() {
    Log.d("MotherView", this)
}


fun Context.dp2px(dp: Int): Int {
    val density = this.resources.displayMetrics.density
    return (dp * density + 0.5).toInt()
}