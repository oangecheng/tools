package com.ustc.orange.tools

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.PaintDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ustc.zax.base.utils.ViewUtil

class ZaxViewActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_zax_view)
    init()
  }

  private fun init() {
    val list = findViewById<RecyclerView>(R.id.view_list)
    list.layoutManager = LinearLayoutManager(list.context)
    list.adapter = ZaxViewAdapter()
    list.addItemDecoration(ItemDivider(list.context))

    findViewById<View>(R.id.select_entrance).setOnClickListener {
      val visible = list.visibility == View.VISIBLE
      list.visibility = if (visible) View.GONE else View.VISIBLE
    }
  }

  inner class ItemDivider(context: Context) : DividerItemDecoration(context, VERTICAL) {
    init {
      val divider = PaintDrawable(Color.TRANSPARENT)
      divider.intrinsicHeight = ViewUtil.dp2px(2)
      setDrawable(divider)
    }
  }
}