package com.ustc.zax.service.example

import androidx.annotation.IntDef


/**
 * Time: 2023/7/25
 * Author: chengzhi@kuaishou.com
 * 业务biz作用域定义
 */
@IntDef(LiveBizScopes.BASE, LiveBizScopes.MAIN, LiveBizScopes.BIZ)
annotation class LiveBizScopes {
  companion object {
    const val BASE = 0
    const val MAIN = 1
    const val BIZ  = 2
  }
}
