package com.ustc.zax.tool

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject

/**
 * Time: 2024/7/11
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc: 系统工具类
 */
object SystemUtils {

  private const val FLAG = PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
  private const val TAG = "SystemUtils"
  private const val IDENTIFIERS = "identifiers"
  private const val SCHEMES = "schemes"

  /**
   * 判断当前手机是否安装了某个app
   * @param pkgName app的包名
   * @param context 上下文
   */
  @SuppressLint("WrongConstant")
  fun isAppInstalledPkg(context: Context, pkgName: String?) : Boolean {
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


  fun isAppInstalledSchema(context: Context, schema: String) : Boolean {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(schema))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return intent.resolveActivity(context.packageManager) != null
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

  fun launchAppForPkg(context: Context, pkgName: String) {
    val intent = context.packageManager.getLaunchIntentForPackage(pkgName)
    if (intent != null) {
      context.startActivity(intent)
    }
  }



  /**
   * Time: 2024/7/12
   * Author: chengzhi@kuaishou.com
   * Beautiful is better than ugly ~
   * Desc: 已安装的app列表
   */
  class CheckInstalledAppsResult {
    @SerializedName("installedApps")
    var apps = ArrayList<String>()
  }


  /**
   *
   */
  fun checkInstalledApps(
    context: Context,
    params: String?,
    callback: (CheckInstalledAppsResult) -> Unit
  ) {

    fun onJsonError() {
      XLog.e(TAG, "json error", IllegalArgumentException())
    }

    fun parseJson(obj: JSONObject, key: String, list: MutableList<String>) {
      val arr = obj.optJSONArray(key)
      if (arr != null && arr.length() > 0) {
        for (i in 0 until arr.length()) {
          arr.optString(i, null)?.let {
            list.add(it)
          }
        }
      }
    }

    if (params.isNullOrEmpty()) {
      onJsonError()
      return
    }

    val identifiers = ArrayList<String>()
    val schemes = ArrayList<String>()

    try {
      val jsonObj = JSONObject(params)
      // 解析包名+scheme
      parseJson(jsonObj, IDENTIFIERS, identifiers)
      parseJson(jsonObj, SCHEMES, schemes)
    } catch (e: JSONException) {
      onJsonError()
      return
    }

    // 数据都为空，则返回error
    if (identifiers.isEmpty() && schemes.isEmpty()) {
      onJsonError()
      return
    }

    val pkgManager = context.packageManager
    val installedApps = ArrayList<String>()
    // check包名类型的
    identifiers.forEach {
      val intent = pkgManager.getLaunchIntentForPackage(it)
      if (intent != null) {
        installedApps.add(it)
      }
    }

    // 判断scheme类型的
    schemes.forEach {
      try {
        val uri = Uri.parse(it)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // 有返回值就认为有安装的数据
        if (intent.resolveActivity(pkgManager) != null) {
          installedApps.add(it)
        }
      } catch (e: Exception) {
        XLog.e(
          TAG, "checkInstalledApps scheme error", e,
          "scheme" to it
        )
      }
    }

    XLog.d(TAG, "installedApps", "apps" to installedApps)
    callback.invoke(
      CheckInstalledAppsResult().also {
        it.apps = installedApps
      }
    )
  }
}