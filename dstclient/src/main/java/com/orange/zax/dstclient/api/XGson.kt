package com.orange.zax.dstclient.api

import com.google.gson.Gson

/**
 * Time: 2024/12/3
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
object XGson {

  val GSON = Gson()

  fun <T> parse(data: String?, cls: Class<T>): T? {
    return try {
      GSON.fromJson<T>(data, cls)
    } catch (e: Exception) {
      null
    }
  }
}