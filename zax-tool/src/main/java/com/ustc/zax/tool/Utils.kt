package com.ustc.zax.tool

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * Time: 2024/7/18
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
object Utils {

  private val HANDLER = Handler(Looper.getMainLooper())

  fun delayTask(delay: Long, token: Any, task: Runnable) {
    HANDLER.sendMessageDelayed(Message.obtain(HANDLER, task).also {
      it.obj = token
    }, delay)
  }

  fun delayTask(delay: Long, task: Runnable) {
    HANDLER.postDelayed(task, delay)
  }

  fun cancelTask(token: Any?) {
    HANDLER.removeCallbacksAndMessages(token)
  }
}