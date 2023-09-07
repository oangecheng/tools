package com.orange.zax.dstclient.api

import android.app.Activity
import android.content.Context
import android.util.Log
import com.orange.zax.dstclient.utils.DstAlert
import com.orange.zax.dstclient.utils.ToastUtil
import io.reactivex.functions.Consumer

/**
 * Time: 2023/9/7
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class DstException  {
}

/**
 * @param context Activity
 */
class ErrorConsumer(
  private val context: Activity? = null
) : Consumer<Throwable> {
  override fun accept(t: Throwable?) {
    Log.e(TestConfig.TAG, "request error", t)
    val msg = t?.message ?: "未知错误"
    if (context != null) {
      DstAlert.alert(context, msg)
    } else {
      ToastUtil.showShort(msg)
    }
  }
}