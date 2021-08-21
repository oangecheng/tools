package com.ustc.orange.tools

import android.graphics.Color
import android.graphics.drawable.PaintDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ustc.zax.base.utils.ViewUtil

class ZaxViewActivity : AppCompatActivity(), ZaxViewAdapter.Listener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_zax_view)
    init()
  }

  private fun init() {
    val list = findViewById<RecyclerView>(R.id.view_list)

    val adapter = ZaxViewAdapter().apply {
      setListener(this@ZaxViewActivity)
    }

    val decoration = DividerItemDecoration(list.context, VERTICAL).apply {
      val divider = PaintDrawable(Color.TRANSPARENT)
      divider.intrinsicHeight = ViewUtil.dp2px(2)
      setDrawable(divider)
    }

    list.layoutManager = LinearLayoutManager(list.context)
    list.adapter = adapter
    list.addItemDecoration(decoration)

    findViewById<View>(R.id.select_entrance).setOnClickListener {
      val visible = list.visibility == View.VISIBLE
      list.visibility = if (visible) View.GONE else View.VISIBLE
    }
  }

  override fun onAddView(view: View?) {
    val container = findViewById<LinearLayout>(R.id.container)
    container.removeAllViews()
    if (view != null) {
      container.addView(view)
    }
  }
}