package com.moreecare.calendarlibrary.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.moreecare.moreeui.calendar.*
import java.util.*


class MonthView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var mSelectDayListener: SelectDayListener? = null

    //正常天数
    val dayPaint = Paint()
    val todayPaint = Paint()
    val mSelectPaint = Paint()


    val bgPaint = Paint()
    val bgSelectPaint = Paint()
    val bgWidth = 48f
    val radius = 36f

    //不是当月的数据
    val mNotCurrentDayPaint = Paint()
    private var mSelectCalendar: Calendar? = null

    val mTextSize = 40f
    var mMonth = 0
    var mYear = 0
    var mDay = 0
    var mCurrentYear = 0
    var mPreMonth = 0
    var mNextMonth = 0

    //当天
    var mCurrentCalendar: Calendar = Calendar.getInstance()

    //这个月最大天数
    var mMaxDayMonth = 30
    var weekDays = 7
    var mCellWidth = 0
    var mCellHeight = 0
    val mWH = 1 / 1

    //选择的画笔
    val selectDayPaint = Paint()

    //这个月第一天星期几
    var firstDayOfMonthWeek = 1

    //星期几开始(默认星期日1开始

    var weekStart = 1
    var offDay = 0
    var TAG = "自定义MonthView"
    var mLineHeight = 0f
    val mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

    init {
        initDayPaint()
        initNCurrentMonthPaint()
        initToady()
        initSelectPaint()

        val fontMetrics = dayPaint.fontMetrics
        mLineHeight = fontMetrics.ascent + fontMetrics.descent
    }

    private fun initSelectPaint() {
        mSelectPaint.isAntiAlias = true
        mSelectPaint.textAlign = Paint.Align.CENTER
        mSelectPaint.color = Color.WHITE
        mSelectPaint.textSize = mTextSize
    }

    private fun initToady() {
        todayPaint.isAntiAlias = true
        todayPaint.textAlign = Paint.Align.CENTER
        todayPaint.color = Color.parseColor("#2CA58B")
        todayPaint.textSize = mTextSize
        bgPaint.isAntiAlias = true
        bgPaint.color = Color.parseColor("#DAF8E7")
        bgSelectPaint.isAntiAlias = true
        bgSelectPaint.color = Color.parseColor("#00C89E")
    }

    /**
     * 不是本月
     */
    private fun initNCurrentMonthPaint() {
        mNotCurrentDayPaint.textSize = mTextSize
        mNotCurrentDayPaint.color = Color.parseColor("#BFBFBF")
        mNotCurrentDayPaint.textAlign = Paint.Align.CENTER
        mNotCurrentDayPaint.isAntiAlias = true
    }

    //本月天数画笔
    private fun initDayPaint() {
        dayPaint.isAntiAlias = true
        dayPaint.textAlign = Paint.Align.CENTER
        dayPaint.color = Color.parseColor("#333333")
        dayPaint.textSize = mTextSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)//view 总宽
        mCellWidth = width / weekDays
        mCellHeight = mCellWidth
        val totalHeight = mCellHeight * 6
        setMeasuredDimension(width, totalHeight)
    }

    var calendars = ArrayList<DayCBean>()

    fun setMonthParameter(calendar: Calendar = Calendar.getInstance(), selectCalendar: Calendar?) {
        calendars.clear()
        mYear = calendar.getYear()
        mMonth = calendar.getMonth()
        mDay = calendar.getDay()
        mMaxDayMonth = calendar.getMaxDay()
        getMonthFirstDay(calendar)
        offDay = firstDayOfMonthWeek - weekStart
        //上个月
        if (offDay > 0) {
            calendar.add(Calendar.MONTH, -1)
            Log.d(TAG, "上个月是:${calendar.getMonth()}")
            getCalendarRang(calendar, calendar.getMaxDay() - offDay + 1, calendar.getMaxDay())
            calendar.add(Calendar.MONTH, 1)
        }
        //本月
        getCalendarRang(calendar, 1, calendar.getMaxDay())
        //下个月
        calendar.add(Calendar.MONTH, 1)
        val nextMonthDay = 42 - calendars.size
        if (nextMonthDay > 0) {
            getCalendarRang(calendar, 1, nextMonthDay)
        }
        this.mSelectCalendar = selectCalendar
        invalidate()
    }

    /**
     * 选择日期
     */
    fun setSelectDay(calendar: Calendar) {
        this.mSelectCalendar = calendar
        invalidate()
    }


    private fun getCalendarRang(calendar: Calendar, startDay: Int, endDay: Int) {
        for (day in startDay..endDay) {
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendars.add(
                DayCBean(
                    calendar.getYear(),
                    calendar.getMonth(),
                    calendar.getDay(),
                    isCurrentDay = isCurrentDay(calendar),
                    isCurrentMon = calendar.get(Calendar.MONTH) == mMonth
                )
            )
        }
    }

    private fun isCurrentDay(calendar: Calendar): Boolean {
        return mCurrentCalendar.getYear() == calendar.getYear() &&
                mCurrentCalendar.getMonth() == calendar.getMonth()
                && mCurrentCalendar.getDay() == calendar.getDay()
    }


    private fun getMonthFirstDay(calendar: Calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        firstDayOfMonthWeek = calendar.get(Calendar.DAY_OF_WEEK)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var row = 0
        var cell = 0

        canvas?.apply {
            calendars.forEachIndexed { index, dayCBean ->
                val x = mCellWidth * cell + mCellWidth.toFloat() / 2
                val y = mCellHeight * row + mCellHeight.toFloat() / 2
                val bgRectF = RectF(x - bgWidth, y - bgWidth, x + bgWidth, y + bgWidth)
                drawSingleSelectBg(dayCBean)

                if (isSelectDay(dayCBean) && dayCBean.isCurrentMon) {
                    drawRoundRect(bgRectF, radius, radius, bgSelectPaint)
                    Log.d(TAG, "选择了:${mSelectCalendar?.getDay()}号 ")
                }
                when {
                    dayCBean.isCurrentDay && dayCBean.isCurrentMon -> {
                        if (!isSelectDay(dayCBean)) {
                            drawRoundRect(bgRectF, radius, radius, bgPaint)
                        }
                        val paint = if (isSelectDay(dayCBean)) {
                            mSelectPaint
                        } else {
                            todayPaint
                        }
                        drawText("今", x, y - mLineHeight * 0.5f, paint)
                    }
                    dayCBean.isCurrentMon -> {
                        val paint = if (isSelectDay(dayCBean)) {
                            mSelectPaint
                        } else {
                            dayPaint
                        }
                        drawText(dayCBean.day.toString(), x, y - mLineHeight * 0.5f, paint)
                    }
                    else -> {
                        drawText(
                            dayCBean.day.toString(),
                            x,
                            y - mLineHeight * 0.5f,
                            mNotCurrentDayPaint
                        )
                    }
                }
                cell++
                if (cell == 7) {
                    cell = 0
                    row++
                }
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                }
                MotionEvent.ACTION_MOVE -> {

                }
                MotionEvent.ACTION_UP -> {
                    clickDay()
                    Log.d(TAG, "onTouchEvent: ACTION_UP超过最新滑动距离")

                }
                else -> {
                }
            }
        }
        return true

    }

    private fun MotionEvent.clickDay() {
        val col = x.toInt() / mCellWidth //第几列
        val row = y.toInt() / mCellHeight//第几行
        val whichNumber = row * 7 + col//算出第几个
//        if (calendars.isEmpty()) {
//            return super.onTouchEvent(event)
//        }
        val dayCBean = calendars[whichNumber]
        if (dayCBean.isCurrentMon) {
            val instance = Calendar.getInstance()
            instance.set(dayCBean.year, dayCBean.month, dayCBean.day)
            mSelectCalendar = instance
            invalidate()
        }
        Log.d(TAG, "选择的第几个:${whichNumber}--   ${dayCBean} ")
        mSelectDayListener?.onSelectDayListener(dayCBean)
    }


    private fun isSelectDay(dayCBean: DayCBean): Boolean {
        if (mSelectCalendar == null) {
            return false
        }

        return mSelectCalendar!!.getYear() == dayCBean.year && mSelectCalendar!!.getMonth() == dayCBean.month &&
                mSelectCalendar!!.getDay() == dayCBean.day
    }

    /**
     * 绘制
     */
    private fun drawSingleSelectBg(dayCBean: DayCBean) {

    }

    fun setSelectDayListener(selectDayListener: SelectDayListener?) {
        mSelectDayListener = selectDayListener
    }

}