package com.moreecare.calendarlibrary.calendar

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * 创建日期：2019/12/3 on 11:47
 * 描述: 简单RecycleView  Adapter
 * 作者:wangKangXu
 */
abstract class SimpleAdapter<D>(private val context: Context, @LayoutRes val layId: Int) :
    RecyclerView.Adapter<SimpleAdapter<D>.VH>() {
    val mList: ArrayList<D> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(layId, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = mList[position]
        cBindViewHolder(holder, data, position)
    }

    abstract fun cBindViewHolder(holder: VH, data: D, position: Int)

    /**
     * 设置data
     */
    fun setData(data: ArrayList<D>): Unit {
        mList.clear()
        mList.addAll(data)
        notifyDataSetChanged()
    }

    /**
     * 添加
     */
    fun addData(data: ArrayList<D>): Unit {
        mList.addAll(data)
        notifyDataSetChanged()
    }

    inner class VH(private val vhitemView: View) : RecyclerView.ViewHolder(vhitemView) {

    }
}