package com.ustc.zax.tool

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

/**
 * Time: 2024/7/11
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc: 系统工具类
 */
object SystemUtils {

  private const val FLAG = PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
  private const val TAG = "SystemUtils"

  /**
   * 判断当前手机是否安装了某个app
   * @param pkgName app的包名
   * @param context 上下文
   */
  @SuppressLint("WrongConstant")
  fun isAppInstalled(context: Context, pkgName : String?) : Boolean {
    return if (!pkgName.isNullOrEmpty()) {
      try {
        context.packageManager.getPackageInfo(pkgName, FLAG)
        true
      } catch (e : Exception) {
        XLog.e(TAG, "isAppInstalled error", e, "pkgName" to pkgName)
        false
      }
    } else {
      false
    }
  }

  /**
   * DESC: 通过schema打开某个app
   * 可以查看对应app的Manifest.xml文件当中定义的协议
   * @param context 上下文
   * @param schema 协议
   */
  fun lunchApp(context: Context, schema: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(schema))
    val infos = context.packageManager.queryIntentActivities(
      intent, 0
    )
    if (infos.isNotEmpty()) {
      context.startActivity(intent)
    } else {
      XLog.e(
        TAG, "lunchAppFail",
        Exception("no such activity"),
        "schema" to  schema
      )
    }
  }
}