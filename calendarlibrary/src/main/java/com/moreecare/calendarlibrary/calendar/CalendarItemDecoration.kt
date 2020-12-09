package com.moreecare.calendarlibrary.calendar

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moreecare.moreeui.calendar.dp2px

class CalendarItemDecoration(context: Context, private val headTitle: HeadTitleProvide) :
    RecyclerView.ItemDecoration() {
    var mDivPaint = Paint()
    private val titlePaint = Paint()
    private val heightDiv = context.dp2px(30)
    private val fontSize = context.dp2px(16)

    init {
        mDivPaint.color = Color.WHITE
        titlePaint.isAntiAlias = true
        titlePaint.color = Color.BLACK
        titlePaint.typeface = Typeface.DEFAULT_BOLD
        titlePaint.textAlign = Paint.Align.CENTER
        titlePaint.textSize = fontSize.toFloat()
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (index in 0..childCount) {
            val childView = parent.getChildAt(index)
            drawDivider(c, childView, parent)
        }
    }

    private fun drawDivider(c: Canvas, childView: View?, parent: RecyclerView) {
        if (childView == null) {
            return
        }
        val left = childView.left
        val top = childView.top
        val right = childView.right

        c.drawRect(Rect(left, top - heightDiv, right, top), mDivPaint)
        c.drawText(
            headTitle.getHeadTitle(parent.getChildAdapterPosition(childView)),
            parent.measuredWidth * 0.5f,
            top - heightDiv * 0.5f,
            titlePaint
        )
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val childView = parent.getChildAt(0)
        val postion = parent.getChildAdapterPosition(childView)
        if (childView.bottom <= heightDiv) {
            c.drawRect(Rect(0, 0, parent.measuredWidth, childView.bottom), mDivPaint)
            val fontMetrics = titlePaint.fontMetrics
            c.drawText(
                "${postion + 1}月",
                parent.measuredWidth * 0.5f,
                childView.bottom.toFloat() - heightDiv * 0.5f,
                titlePaint
            )
        } else {
            c.drawRect(Rect(0, 0, parent.measuredWidth, heightDiv), mDivPaint)
            c.drawText(
                "${postion + 1}月",
                parent.measuredWidth * 0.5f,
                heightDiv * 0.5f,
                titlePaint
            )
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, heightDiv, 0, 0)
    }

    interface HeadTitleProvide {
        fun getHeadTitle(childAdapterPosition: Int): String
    }
}