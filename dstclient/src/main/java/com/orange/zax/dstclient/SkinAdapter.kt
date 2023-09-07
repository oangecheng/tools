package com.orange.zax.dstclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.zax.dstclient.data.Skin
import com.ustc.zax.base.recycler.BaseRecyclerAdapter

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class SkinAdapter : BaseRecyclerAdapter<Skin, ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.dst_skin_item_layout, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let {
      holder.bind(it)
    }
  }
}


class ViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {
  private val name = view.findViewById<TextView>(R.id.skin_name)
  private val check = view.findViewById<CheckBox>(R.id.skin_select)

  fun bind(skin: Skin) {
    view.setOnClickListener {
      check.isChecked = !check.isChecked
      skin.isSelected = check.isChecked
    }
    name.text = skin.skinName
    check.setOnClickListener {
      skin.isSelected = check.isChecked
    }
  }
}