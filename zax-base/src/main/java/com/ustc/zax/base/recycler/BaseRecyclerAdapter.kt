package com.ustc.zax.base.recycler

import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * 基础RecyclerAdapter
 * 封装了一些常用的方法，便于接入方调用
 *
 * @param MODEL 数据类
 * @param VH holder
 */
abstract class BaseRecyclerAdapter<MODEL, VH : RecyclerView.ViewHolder>
  : RecyclerView.Adapter<VH> {

  private var mList: MutableList<MODEL> = ArrayList()
  private var mDispatchModify = false

  constructor(dispatchModify: Boolean) {
    mDispatchModify = dispatchModify
  }

  constructor() : this(true)

  fun setList(list: List<MODEL>) {
    mList.clear()
    mList.addAll(list)
  }

  fun getList(): List<MODEL> {
    return mList
  }

  override fun getItemCount(): Int {
    return mList.size
  }

  fun isEmpty(): Boolean {
    return mList.isEmpty()
  }

  fun getItem(position: Int): MODEL? {
    return if (position < 0 || position >= mList.size) null else mList[position]
  }

  fun getItemPosition(item: MODEL): Int {
    return mList.indexOf(item)
  }

  fun add(item: MODEL): BaseRecyclerAdapter<MODEL, VH> {
    mList.add(item)
    if (!mDispatchModify) {
      return this
    }
    if (mList.size > 0) {
      notifyItemInserted(mList.size - 1)
    }
    return this
  }

  fun add(position: Int, item: MODEL): BaseRecyclerAdapter<MODEL, VH> {
    mList.add(position, item)
    if (!mDispatchModify) {
      return this
    }
    notifyItemInserted(position)
    return this
  }

  fun remove(position: Int): BaseRecyclerAdapter<MODEL, VH> {
    mList.removeAt(position)
    if (!mDispatchModify) {
      return this
    }
    notifyItemRemoved(position)
    return this
  }

  fun remove(item: MODEL): BaseRecyclerAdapter<MODEL, VH> {
    val position = mList.indexOf(item)
    mList.remove(item)
    if (!mDispatchModify) {
      return this
    }
    if (position != -1) {
      notifyItemRemoved(position)
    }
    return this
  }

  fun removeList(position: Int, count: Int): BaseRecyclerAdapter<MODEL, VH> {
    mList.subList(position, position + count).clear()
    if (!mDispatchModify) {
      return this
    }
    notifyItemRangeRemoved(position, count)
    return this
  }

  fun set(index: Int, item: MODEL): BaseRecyclerAdapter<MODEL, VH> {
    mList[index] = item
    if (!mDispatchModify) {
      return this
    }
    notifyItemChanged(index)
    return this
  }

  fun addAll(items: List<MODEL>): BaseRecyclerAdapter<MODEL, VH> {
    val startIndex = if (mList.size > 0) mList.size - 1 else 0
    mList.addAll(items)
    if (!mDispatchModify) {
      return this
    }
    if (startIndex >= 0) {
      notifyItemRangeInserted(startIndex, items.size)
    }
    return this
  }

  fun addAll(items: Collection<MODEL>): BaseRecyclerAdapter<MODEL, VH> {
    val startIndex = if (mList.size > 0) mList.size - 1 else 0
    mList.addAll(items)
    if (!mDispatchModify) {
      return this
    }
    if (startIndex >= 0) {
      notifyItemRangeInserted(startIndex, items.size)
    }
    return this
  }

  fun removeAll(item: MODEL): BaseRecyclerAdapter<MODEL, VH> {
    val iterator: MutableIterator<*> = mList.iterator()
    while (iterator.hasNext()) {
      if (item == iterator.next()) {
        iterator.remove()
      }
    }
    if (!mDispatchModify) {
      return this
    }
    notifyDataSetChanged()
    return this
  }

  fun clear(): BaseRecyclerAdapter<MODEL, VH> {
    val size = mList.size
    mList.clear()
    if (!mDispatchModify) {
      return this
    }
    notifyItemRangeRemoved(0, size)
    return this
  }
}