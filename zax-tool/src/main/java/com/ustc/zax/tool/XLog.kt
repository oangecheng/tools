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

    fun d(
      subTag: String, msg: String,
      k1: String, v1: Any,
      k2: String? = null, v2: Any? = null
    ) {
      if (k2 != null && v2 != null) {
        d(subTag, msg, k1 to v1, k2 to v2)
      } else {
//        d(subTag, msg, k1 to v1, k2 to v2)
      }
    }

    fun d(
      subTag: String, msg: String,
      k1: String, v1: Any,
      k2: String, v2: Any,
      k3: String? = null, v3: Any? = null
    ) {
      d(subTag, msg, k1, v1, k2, v2, k3, v3, null, null)
    }

    fun d(
      subTag: String, msg: String,
      k1: String?, v1: Any?,
      k2: String?, v2: Any?,
      k3: String?, v3: Any?,
      k4: String? = null, v4: Any? = null
    ) {

    }

    fun d(subTag : String, msg : String, vararg data : Pair<String, Any?>) {
      Log.d("$TAG $subTag", "$msg ${parseData(*data)}")
    }

    fun e(subTag: String, msg: String, e : Throwable, vararg data : Pair<String, Any>) {
      Log.e("$TAG $subTag", "$msg ${parseData(*data)}", e)
    }

    private fun parseData(vararg data : Pair<String, Any?>) : String {
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