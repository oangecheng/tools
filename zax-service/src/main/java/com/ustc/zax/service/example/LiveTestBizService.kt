package com.ustc.zax.service.example

import android.util.Log

/**
 * Time: 2023/7/22
 * Author: chengzhi@kuaishou.com
 * 测试使用的Service
 */
interface LiveTestBizService : LiveBizService {

  @JvmDefault
  override fun scope(): Int {
    return LiveBizScopes.MAIN
  }

  @JvmDefault
  fun show(name: String) {
    Log.d("orangeInvoke", "biz service default show $name")
  }

  @JvmDefault
  fun getInt(): Int {
    return 1000
  }

  @JvmDefault
  fun getBoolean(): Boolean {
    return true
  }

  @JvmDefault
  fun getLong(): Long {
    return 1234L
  }

  @JvmDefault
  fun getFloat(): Float {
    return 0.5f
  }

  @JvmDefault
  fun getString(): String {
    return "哈哈"
  }
}