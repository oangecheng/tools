package com.ustc.orange.tools.test

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.Toast
import com.ustc.zax.tool.XLog

/**
 * Time: 2024/8/23
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class XBroadcastTest: XTestModel {

  companion object {
    private const val TAG = "XBroadcastTest"
    private const val FILTER = "debug.kwai.live.adb"
    private const val ACTION_KEY = "action"
  }

  annotation class ACTIONS {
    companion object {
      const val CLEAR_SP = "CLEAR_SP";
      const val TOAST = "TOAST"
    }
  }

  private val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      val act = intent?.getStringExtra(ACTION_KEY)?:return
      when(act) {
        ACTIONS.CLEAR_SP -> {
          val key = intent.getStringExtra("data")
          XLog.d(TAG, "清理sp key=$key")
        }
        ACTIONS.TOAST -> {
          val msg = intent.getStringExtra("data")
          Toast.makeText(context, "$msg", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  @SuppressLint("UnspecifiedRegisterReceiverFlag")
  override fun onStart(context: Context) {
    val filter = IntentFilter(FILTER)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.registerReceiver(receiver, filter, 0)
    } else {
      context.registerReceiver(receiver, filter)
    }
  }

  override fun onStop() {

  }
}