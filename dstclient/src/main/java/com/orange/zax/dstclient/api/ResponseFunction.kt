package com.orange.zax.dstclient.api

import android.util.Log
import io.reactivex.functions.Function

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
class ResponseFunction<T> : Function<Response<T>, T> {
  @Throws(Exception::class)
  override fun apply(t: Response<T>): T {
    Log.d("OrangeTest", t.toString())
    if(t.status == 1) {
      return t.data ?: ActionResponse(t.status) as T
    } else {
      throw Exception(t.msg)
    }
  }
}