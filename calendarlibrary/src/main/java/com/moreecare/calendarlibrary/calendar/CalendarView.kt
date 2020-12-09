package com.moreecare.calendarlibrary.calendar

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.moreecare.calendarlibrary.R
import kotlinx.android.synthetic.main.item_calendar_.view.*
import kotlinx.android.synthetic.main.layout_calendar_view.view.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mSelectListener: SelectDayListener? = null
    private var mSelectCalendar: Calendar? = null
    var years = ArrayList<Int>()
    private val mAdapter = object : SimpleAdapter<Int>(context, R.layout.item_calendar_) {
        override fun cBindViewHolder(holder: VH, data: Int, position: Int) {
            holder.itemView.calendar_view.setYear(data, mSelectCalendar)
            holder.itemView.calendar_view.setSelectDayListener(object : SelectDayListener {
                override fun onSelectDayListener(dayCBean: DayCBean) {
                    mSelectListener?.onSelectDayListener(dayCBean)
                    val instance = Calendar.getInstance()
                    instance.set(dayCBean.year, dayCBean.month, dayCBean.day)
                    this@CalendarView.mSelectCalendar = instance
                    notifyDataSetChanged()
                }
            })
        }

    }

    init {
        inflate(context, R.layout.layout_calendar_view, this)
        vp2.adapter = mAdapter
        vp2.overScrollMode = OVER_SCROLL_NEVER
        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (years.isNotEmpty()) {
                    tv_current_year.text = "${years[position]}年"
                }
            }
        })

    }


    fun setYearRange(yearRange: IntRange, selectDayListener: SelectDayListener) {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        years = arrayListOf()
        for (yearTemp in yearRange) {
            years.add(yearTemp)
        }
        mAdapter.setData(years)
        Log.d("TAG", "setYearRange:${years} ")
        vp2.currentItem = years.indexOf(year)
        this.mSelectListener = selectDayListener
    }
}