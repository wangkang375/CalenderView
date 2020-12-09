package com.moreecare.calendarlibrary.calendar

import android.content.Context
import android.util.AttributeSet
import android.util.Range
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moreecare.calendarlibrary.R
import com.moreecare.moreeui.calendar.getDay
import com.moreecare.moreeui.calendar.getMonth
import com.moreecare.moreeui.calendar.getYear
import kotlinx.android.synthetic.main.item_calendar_year.view.*
import java.util.*

class YearView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), CalendarItemDecoration.HeadTitleProvide {

    var initFlag = false
    private var mSelectDayListener: SelectDayListener? = null
    private var mSelectCalendar: Calendar? = null
    private var mMonthAdapter =
        object : SimpleAdapter<Calendar>(context, R.layout.item_calendar_year) {
            override fun cBindViewHolder(holder: VH, data: Calendar, position: Int) {
                val calendar = Calendar.getInstance()
                calendar.set(data.getYear(), data.getMonth(), data.getDay())
                holder.itemView.monthViewItem.setMonthParameter(calendar, mSelectCalendar)
                holder.itemView.monthViewItem.setSelectDayListener(mSelectDayListener)
            }
        }

    init {
        layoutManager = LinearLayoutManager(context)
        overScrollMode = OVER_SCROLL_NEVER
        adapter = mMonthAdapter
        addItemDecoration(CalendarItemDecoration(context, this))
    }

    var calendars = arrayListOf<Calendar>()
    fun setYear(year: Int, selectCalendar: Calendar?) {
        calendars = arrayListOf()
        for (month in 0..11) {
            val instance = Calendar.getInstance()
            instance.set(Calendar.MONTH, month)
            instance.set(Calendar.YEAR, year)
            calendars.add(instance)
        }
        this.mSelectCalendar = selectCalendar
        mMonthAdapter.setData(calendars)
        val tempCalendar = Calendar.getInstance()
        val yearT = tempCalendar[Calendar.YEAR]
        val monthT = tempCalendar[Calendar.MONTH]
        if (yearT == year && !initFlag) {
            initFlag = true
            layoutManager?.scrollToPosition(monthT)
        }
    }

    fun setSelectDayListener(selectDayListener: SelectDayListener) {
        mSelectDayListener = selectDayListener
    }

    override fun getHeadTitle(childAdapterPosition: Int): String {
        return "${calendars[childAdapterPosition][Calendar.MONTH] + 1}月"
    }
}