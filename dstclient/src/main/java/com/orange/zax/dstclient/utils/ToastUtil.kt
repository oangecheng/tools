package com.orange.zax.dstclient.utils

import android.widget.Toast
import com.ustc.zax.base.GlobalConfig

/**
 * Time: 2023/9/6
 * Author: chengzhi@kuaishou.com
 */
class ToastUtil {

  companion object {
    fun showShort(msg : String) {
      Toast.makeText(GlobalConfig.application, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLong(msg : String) {
      Toast.makeText(GlobalConfig.application, msg, Toast.LENGTH_LONG).show()
    }
  }
}