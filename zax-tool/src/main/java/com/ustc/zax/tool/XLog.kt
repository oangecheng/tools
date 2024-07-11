package com.ustc.zax.tool

import android.util.Log

/**
 * Time: 2024/7/11
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
class XLog {

  companion object {

    private const val TAG = "XLogInfo"

    fun d(subTag : String, msg : String, vararg data : Pair<String, Any>) {
      Log.d("$TAG $subTag", "$msg ${parseData(*data)}")
    }

    fun e(subTag: String, msg: String, e : Throwable, vararg data : Pair<String, Any>) {
      Log.e("$TAG $subTag", "$msg ${parseData(*data)}", e)
    }

    private fun parseData(vararg data : Pair<String, Any>) : String {
      return if (data.isNotEmpty()) {
        data.joinToString(" | ") {
          "${it.first}=${it.second}"
        }
      } else {
        ""
      }
    }
  }
}