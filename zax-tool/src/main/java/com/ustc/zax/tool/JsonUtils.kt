package com.ustc.zax.tool

import androidx.collection.ArraySet
import org.json.JSONObject

/**
 * Time: 2023/7/21
 * Author: chengzhi@kuaishou.com
 * Json相关的工具类
 */
class JsonUtils {

  /**
   * 比较两个json字符串字段是否对齐
   * @param jsonA 数据1
   * @param jsonB 数据2
   */
  fun compare(jsonA : String, jsonB: String) : ArraySet<String> {
    val objA = JSONObject(jsonA)
    val objB = JSONObject(jsonB)

    val different = ArraySet<String>()
    objA.keys().forEach {
      if (!objB.has(it)) {
        different.add("A $it")
      }
    }

    objB.keys().forEach {
      if ((!objA.has(it))) {
        different.add("B $it")
      }
    }

    return different
  }
}