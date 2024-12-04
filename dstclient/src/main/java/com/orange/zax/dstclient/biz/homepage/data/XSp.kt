package com.orange.zax.dstclient.biz.homepage.data

import android.content.Context
import android.content.SharedPreferences
import com.ustc.zax.base.GlobalConfig


/**
 * Time: 2024/12/3
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
object XSp {
  private const val KEY = "X_DST_DATA"
  private const val P_KEY = "PREFABS"

  private val sp = GlobalConfig.application.getSharedPreferences(KEY, Context.MODE_PRIVATE)


  fun get(): SharedPreferences {
    return sp
  }

}