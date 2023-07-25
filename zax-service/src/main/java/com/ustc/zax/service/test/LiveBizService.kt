package com.ustc.zax.service.test

import android.util.Log
import com.ustc.zax.service.BizService

/**
 * Time: 2023/7/22
 * Author: chengzhi@kuaishou.com
 * 测试使用的Service
 */
interface LiveBizService: BizService {

  @JvmDefault
  override fun create(): BizService {
    return object : LiveBizService {}
  }

  @JvmDefault
  fun show(name : String) {
    Log.d("orangeInvoke", "biz service default show")
  }

  @JvmDefault
  fun getInt() : Int {
    return 1000
  }

  @JvmDefault
  fun getBoolean() : Boolean {
    return true
  }

  @JvmDefault
  fun getLong() : Long {
    return 0
  }

  @JvmDefault
  fun getFloat() : Float {
    return 0.5f
  }

  @JvmDefault
  fun getString() : String {
    return "哈哈"
  }
}