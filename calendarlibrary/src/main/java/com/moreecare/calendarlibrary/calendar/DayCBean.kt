package com.moreecare.calendarlibrary.calendar

import java.util.*

data class DayCBean(
    var year: Int,
    var month: Int,
    var day: Int,
    var isCurrentDay: Boolean,
    var isCurrentMon: Boolean = true
)
