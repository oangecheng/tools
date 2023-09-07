package com.orange.zax.dstclient.utils

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.orange.zax.dstclient.AdminAccount

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

    fun adminCheck(valid: (name: String, pwd: String) -> Unit) {
      val adminName = AdminAccount.name()
      val adminPwd = AdminAccount.pwd()
      if (TextUtils.isEmpty(adminName) || TextUtils.isEmpty(adminPwd)) {
        ToastUtil.showShort("未登录，不可操作!")
      } else {
        valid.invoke(adminName, adminPwd)
      }
    }
  }
}