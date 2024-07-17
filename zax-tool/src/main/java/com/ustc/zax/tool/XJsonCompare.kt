package com.ustc.zax.tool

import org.json.JSONObject

/**
 * Time: 2023/7/21
 * Author: chengzhi@kuaishou.com
 * Json比较相关的工具类
 */
class XJsonCompare {

  companion object {
    private const val TAG = "XJsonCompare"
    private const val JSON1 = "{\n" +
      "  \"key\" : {\n" +
      "    \"k1\" : \"123\",\n" +
      "    \"k2\" : \"124\",\n" +
      "    \"k3\" : \"125\",\n" +
      "    \"k4\" : \"126\",\n" +
      "    \"k5\" : \"127\"\n" +
      "  }\n" +
      "}"
    private const val JSON2 = "{\n" +
      "  \"key\" : {\n" +
      "    \"k1\" : \"120\",\n" +
      "    \"k2\" : \"124\",\n" +
      "    \"k3\" : \"125\",\n" +
      "    \"k4\" : \"126\",\n" +
      "    \"k5\" : \"127\"\n" +
      "  }\n" +
      "}"
  }

  fun test() {
    compareValue(JSON1, JSON2, "key")
  }

  /**
   * 比两个
   */
  private fun compareValue(json1: String,json2 : String,  key: String) {
    val objA = parse(JSONObject(json1)).get(key) as JSONObject
    val objB = parse(JSONObject(json2)).get(key) as JSONObject

    val compareList = ArrayList<Pair<String, String>>()
    objA.keys().forEach {
      val vA = objA.get(it) ?: "null"
      val vB = if (objB.has(it)) {
        objB.get(it)
      } else {
        "null"
      }

      compareList.add(
        it to "android=$vA  iOS=$vB"
      )
    }

    compareList.forEach {
      XLog.d(TAG, "compareValue: " + it.first + "   " + it.second)
    }
  }

  private fun parse(json: JSONObject): JSONObject {
    val ret = JSONObject()
    json.keys().forEach {
      val v = json.get(it)
      if (v is String && isJSONObject(v)) {
        ret.put(it, parse(JSONObject(v)))
      } else {
        ret.put(it, v)
      }
    }
    return ret
  }

  private fun isJSONObject(string: String): Boolean {
    return try {
      JSONObject(string)
      true
    } catch (e: Exception) {
      false
    }
  }
}