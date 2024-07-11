package com.ustc.zax.base.log

import android.util.Log

/**
 * Time: 2024/6/11
 * Author: chengzhi@kuaishou.com
 */
class XLog {

  companion object {
    private const val TAG = "XLog"

    fun info(subTag: String, msg: String, vararg args: Pair<String, Any?>) {
      val str = args.joinToString(" | ") { "${it.first}=${it.second}" }
      Log.d(TAG, "$subTag $msg : $str")
    }

    fun error(subTag: String, msg: String, e: Throwable?, vararg args: Pair<String, Any?>) {
      val str = args.joinToString(" | ") { "${it.first}=${it.second}" }
      Log.d(TAG, "$subTag $msg : $str", e)
    }
  }
}