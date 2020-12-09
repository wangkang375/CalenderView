package com.moreecare.calendarapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.moreecare.calendarlibrary.calendar.CalendarView
import com.moreecare.calendarlibrary.calendar.DayCBean
import com.moreecare.calendarlibrary.calendar.SelectDayListener


class MainActivity : AppCompatActivity(), SelectDayListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<CalendarView>(R.id.cnv).setYearRange(2002..2020, this)
    }

    override fun onSelectDayListener(dayCBean: DayCBean) {
        Toast.makeText(
            this,
            "${dayCBean.year}年${dayCBean.month + 1}月${dayCBean.day}日",
            Toast.LENGTH_SHORT
        ).show()
        Log.d("dayCBean", "dayCBean:${dayCBean.year}年${dayCBean.month + 1}月${dayCBean.day}日")
    }
}