package com.ustc.orange.tools

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.PaintDrawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ustc.zax.base.recycler.BaseRecyclerAdapter
import com.ustc.zax.base.utils.ViewUtil
import com.ustc.zax.view.HollowTextSpan

class ZaxViewAdapter : BaseRecyclerAdapter<ZaxViewAdapter.Item, ZaxViewAdapter.Holder>() {

  private var listener: Listener? = null

  init {
    setList(createItems())
  }

  fun setListener(listener: Listener?) {
    this.listener = listener
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
    list.add(Item("镂空富文本测试", 1))
    list.add(Item("镂空富文本测试", 2))
    list.add(Item("镂空富文本测试", 3))
    return list
  }

  inner class Holder(private var view: TextView) : RecyclerView.ViewHolder(view) {
    fun bind(item: Item) {
      view.text = item.title
      view.setOnClickListener {
        val testView = createTestView(it.context, item.type)
        listener?.onAddView(testView)
      }
    }
  }

  private fun createTestView(context: Context, type: Int): View? {
    return when (type) {
      1 -> createHollowTextSpanTestView(context)
      else -> null
    }
  }

  private fun createHollowTextSpanTestView(context: Context): View {
    val d = PaintDrawable(Color.GREEN).apply {
      setBounds(0, 0, 100, 50)
      setCornerRadius(10f)
    }

    return TextView(context).apply {
      setBackgroundColor(Color.RED)
      textSize = 20f
    }.apply {
      val span = HollowTextSpan("中国人", d)
      val sb = SpannableStringBuilder()
      sb.append("上下五千年历史").append("*")
      sb.setSpan(span, sb.length - 1, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
      sb.append("璀璨的中华文化")
      text = sb
    }
  }

  interface Listener {
    fun onAddView(view: View?)
  }

  data class Item(val title: String, val type: Int)
}