package com.orange.zax.dstclient.utils

import android.app.Activity
import android.content.Intent

/**
 * Time: 2023/9/6
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class Utils {
  companion object {

    fun jumpActivity(from : Activity, to : Class<*>) {
      val intent = Intent()
      intent.setClass(from, to)
      from.startActivity(intent)
    }

    fun jumpActivityAnFinish(from : Activity, to : Class<*>) {
      jumpActivity(from, to)
      from.finish()
    }

    fun emptyIfNull(string: String?) : String {
      return string?:""
    }

    fun safeParseInt(string: String?) : Int {
      return try {
        string?.toInt()?:0
      } catch (e: Exception) {
        0
      }
    }
  }
}