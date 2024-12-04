package com.orange.zax.dstclient.biz.homepage.data

import android.content.Context
import android.content.SharedPreferences
import com.orange.zax.dstclient.api.XGson
import com.ustc.zax.base.GlobalConfig


/**
 * Time: 2024/12/3
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
object XSp {
  private const val KEY = "X_DST_DATA"
  private const val P_KEY = "PREFABS"

  private val sp = GlobalConfig.application.getSharedPreferences(KEY, Context.MODE_PRIVATE)

  private val PREFABS = XGson.parse(
    sp.getString(P_KEY, null), Prefabs::class.java
  )?.prefabs ?: HashMap()

  fun get(): SharedPreferences {
    return sp
  }

  fun getPrefabs() : Map<String, Prefab> {
    return PREFABS
  }

  fun addPrefab(prefab: Prefab) {
    PREFABS[prefab.id] = prefab
    sp.edit().putString(P_KEY, XGson.GSON.toJson(PREFABS)).apply()
  }

}