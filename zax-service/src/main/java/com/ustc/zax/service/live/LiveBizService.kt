package com.ustc.zax.service.live

import com.ustc.zax.tool.XLog

/**
 * Time: 2024/7/17
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
interface LiveBizService : LiveService<LiveBizService> {

  /**
   *
   */
  @JvmDefault
  @LiveServiceAction
  fun addListener(cb: Callback) {
    XLog.d("orangeTest", "addCallback" )

  }

  @JvmDefault
  @LiveServiceAction
  fun removeListener(cb: Callback) {

  }

  @JvmDefault
  fun show() {
    XLog.d("orangeTest", "haha")
  }

  interface Callback {
    fun onStart()
  }
}