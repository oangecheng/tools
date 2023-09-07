package com.orange.zax.dstclient.utils

import android.app.AlertDialog
import android.content.Context

/**
 * Time: 2023/9/7
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class DstAlert {

  companion object {
    fun alert(context: Context, msg: String, cb: () -> Unit) {
      AlertDialog.Builder(context)
        .setMessage(msg)
        .setNegativeButton("取消") { _, _ -> }
        .setPositiveButton("确定") { _, _ -> cb() }
        .show()
    }

    fun alert(context: Context, msg : String) {
      AlertDialog.Builder(context)
        .setMessage(msg)
        .setPositiveButton("我知道了") {_,_->}
        .show()
    }
  }
}