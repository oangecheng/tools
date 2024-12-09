package com.orange.zax.dstclient.biz.homepage

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.zax.dstclient.R
import com.ustc.zax.base.recycler.BaseRecyclerAdapter
import com.ustc.zax.base.utils.ViewUtil

/**
 * Time: 2024/5/30
 * Author: chengzhi@kuaishou.com
 */
class HomeTabSelectDialog : DialogFragment() {

  companion object {
    fun newInstance(data : List<Int>, listener: Listener) : DialogFragment {
      val dialog = HomeTabSelectDialog()
      dialog.data = data
      dialog.listener = listener
      return  dialog
    }
  }

  private lateinit var tabListView: RecyclerView
  private var data : List<Int>? = null
  private var listener: Listener? = null
  private val adapter = TabAdapter()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.dst_homepage_tabs, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val window = dialog?.window
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dp2px(438f))
    window?.setGravity(Gravity.BOTTOM)

    tabListView = view.findViewById(R.id.tab_list)
    initView(tabListView)
  }

  override fun onDismiss(dialog: DialogInterface) {
    super.onDismiss(dialog)
    listener?.onDismiss(
      adapter.getList()
        .filter { it.selected }
        .map { it.tab }
    )
  }

  private fun initView(view: RecyclerView) {
    tabListView.layoutManager = LinearLayoutManager(view.context)
    tabListView.adapter = adapter

    val allItem = TABS.map {
      TabItem(it.key, it.value, false)
    }
    data?.let {
      it.forEach { tab ->
        allItem.firstOrNull { item ->
          item.tab == tab
        }?.selected = true
      }
    }
    adapter.addAll(allItem)
  }

  interface Listener {
    fun onDismiss(tabs : List<Int>)
  }
}

data class TabItem(
  val tab : Int,
  val name : String,
  var selected : Boolean
)


private class TabViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  private val check: CheckBox = view.findViewById(R.id.tab_checkbox)
  private val name: TextView = view.findViewById(R.id.tab_name)

  fun bind(item: TabItem) {
    check.isChecked = item.selected
    name.text = item.name
    check.setOnCheckedChangeListener { _, isChecked ->
      item.selected = isChecked
    }
  }

}


private class TabAdapter : BaseRecyclerAdapter<TabItem, TabViewHolder>() {

  override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TabViewHolder {
    return TabViewHolder(
      LayoutInflater
        .from(p0.context)
        .inflate(
          R.layout.dst_homepage_tabs_item,
          p0,
          false
        )
    )
  }

  override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
    getItem(position)?.let {
      holder.bind(it)
    }
  }
}

val TABS : Map<Int, String> = mapOf(
  1 to "建筑",
  2 to "合成",
  3 to "农场",
  4 to "植物",
  5 to "灵魂",
  6 to "工具",
  7 to "储物"
)