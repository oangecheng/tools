package com.ustc.orange.tools.pages.vc

import android.view.View
import com.kuaishou.live.viewcontroller.ViewController
import com.ustc.orange.tools.R
import com.ustc.zax.tool.Utils

/**
 * Time: 2024/11/20
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class XViewMain : ViewController() {

  private var vc : ViewController? = null


  override fun onCreate() {
    super.onCreate()
    setContentView(R.id.vc_test_main, R.layout.x_vc_test_main)
  }

  override fun reuseView(): Boolean {
    return true
  }

  fun changeViewController(pos: Int) {
    val v = when (pos) {
      1 -> XView1()
      2 -> XView2()
      3 -> XView3()
      4 -> XView4()
      else -> null
    }

    if (vc != null) {
      removeViewController(vc!!)
    }
    if (v != null) {
      addViewController(R.id.vc_test_main, v)
      vc = v
    }
  }
}


class XView1 : ViewController() {
  override fun onCreate() {
    super.onCreate()
    setContentView(R.id.vc_test_1, R.layout.x_vc_test_1)

    Utils.delayTask(2000, Runnable {
      requireContentView().visibility = View.VISIBLE
    })
  }
  override fun reuseView(): Boolean {
    return true
  }

  override fun onDestroy() {
    super.onDestroy()
    requireContentView().visibility = View.GONE
  }
}

class XView2 : ViewController() {
  override fun onCreate() {
    super.onCreate()
    setContentView(R.id.vc_test_2, R.layout.x_vc_test_2)
  }
  override fun reuseView(): Boolean {
    return true
  }
}

class XView3 : ViewController() {
  override fun onCreate() {
    super.onCreate()
    setContentView(R.id.vc_test_3, R.layout.x_vc_test_3)
  }
  override fun reuseView(): Boolean {
    return true
  }
}

class XView4 : ViewController() {
  override fun onCreate() {
    super.onCreate()
    setContentView(R.id.vc_test_4, R.layout.x_vc_test_4)
  }

  override fun reuseView(): Boolean {
    return true
  }
}


