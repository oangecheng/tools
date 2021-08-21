package com.ustc.orange.tools

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ustc.zax.base.recycler.BaseRecyclerAdapter
import com.ustc.zax.base.utils.ViewUtil

class ZaxViewAdapter : BaseRecyclerAdapter<ZaxViewAdapter.Item, ZaxViewAdapter.Holder>() {

  init {
    setList(createItems())
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
    val view = createItemView(parent.context)
    return Holder(view)
  }

  override fun onBindViewHolder(holder: Holder, position: Int) {
    val item = getItem(position)
    if (item != null) {
      holder.bind(item)
    }
  }

  private fun createItemView(context: Context): TextView {
    val view = TextView(context)
    view.setBackgroundColor(Color.WHITE)
    view.layoutParams = ViewGroup.LayoutParams(ViewUtil.dp2px(150), ViewUtil.dp2px(40))
    view.gravity = Gravity.CENTER_VERTICAL
    view.setPadding(ViewUtil.dp2px(10), 0, 0, 0)
    view.textSize = 16f
    view.setTextColor(Color.BLACK)
    return view
  }

  private fun createItems(): List<Item> {
    val list = ArrayList<Item>()
    list.add(Item("镂空富文本测试"))
    list.add(Item("镂空富文本测试"))
    list.add(Item("镂空富文本测试"))
    return list
  }

  inner class Holder(private var view: TextView) : RecyclerView.ViewHolder(view) {
    fun bind(item: Item) {
      view.text = item.title
    }
  }

  data class Item(val title: String)
}