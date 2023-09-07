package com.orange.zax.dstclient.view

import android.os.SystemClock
import android.view.View

/**
 * Time: 2023/9/7
 * Author: chengzhi@kuaishou.com
 */
class ClickFilter(private val click: (v: View) -> Unit) : View.OnClickListener {

  private var lastTime = 0L
  private val limit = 2000L


  override fun onClick(v: View) {
    if (SystemClock.elapsedRealtime() - lastTime > limit) {
      lastTime = SystemClock.elapsedRealtime()
      click(v)
    }
  }
}