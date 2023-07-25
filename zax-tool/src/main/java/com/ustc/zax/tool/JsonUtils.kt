package com.ustc.zax.tool

import android.text.TextUtils
import android.util.Log
import androidx.collection.ArraySet
import org.json.JSONArray
import org.json.JSONObject

/**
 * Time: 2023/7/21
 * Author: chengzhi@kuaishou.com
 * Json相关的工具类
 */
class JsonUtils {

  fun test() {
    val jsonA = "{\"bulletPlayInfo\":\"{\\\"app\\\":\\\"debug\\\",\\\"bitRateAvg\\\":452,\\\"bitRateMax\\\":1404,\\\"captureAvgTime\\\":0,\\\"captureMaxTime\\\":1,\\\"click2ScreenTime\\\":-1,\\\"completeQueueTimeStamp\\\":-1,\\\"connectLostCnt\\\":0,\\\"connectLostCode\\\":-1,\\\"decodeAvgTime\\\":70,\\\"decodeMaxTime\\\":517,\\\"display\\\":\\\"1080P\\\",\\\"displayChange2SuccessDelay\\\":0,\\\"displayChangeCnt\\\":0,\\\"encodeAvgTime\\\":3,\\\"encodeMaxTime\\\":4,\\\"endToEndDelayAvg\\\":37,\\\"endToEndDelayMax\\\":58,\\\"extraFields\\\":\\\"2159586065\\\",\\\"firstScreen\\\":-1,\\\"freshRateAvg\\\":24,\\\"freshRateMin\\\":-1,\\\"homeTime\\\":16,\\\"inPlayErrorMsg\\\":\\\"\\\",\\\"initCloudPlayTime\\\":-1,\\\"kwaiVersion\\\":\\\"chengzhi-0.0.1-beta16\\\",\\\"lossPacketAllCnt\\\":-1,\\\"lossPacketExceptionCnt\\\":-1,\\\"lossRateAvg\\\":0,\\\"lossRateMax\\\":0,\\\"monitorPeriodEndTime\\\":1690202146788,\\\"monitorPeriodStartTime\\\":1690202146786,\\\"networkTrafficAvg\\\":-1,\\\"networkTrafficMax\\\":-1,\\\"networkType\\\":\\\"wifi\\\",\\\"packageName\\\":\\\"pangkezhengba\\\",\\\"phoneModel\\\":\\\"SM-A5160\\\",\\\"platform\\\":\\\"Weier\\\",\\\"playEndTime\\\":1690202146788,\\\"playTotalTime\\\":44616,\\\"playStartTime\\\":1690202102172,\\\"queueStatus\\\":-1,\\\"queueTime\\\":-1,\\\"reconnectCnt\\\":0,\\\"renderAvgTime\\\":68,\\\"renderMaxTime\\\":212,\\\"screenCallBackCnt\\\":0,\\\"screenOrientation\\\":\\\"landscape\\\",\\\"sdkSessionID\\\":\\\"com.smile.gifmaker\\\",\\\"sdkVersion\\\":\\\"6.45.1\\\",\\\"sessionID\\\":\\\"a681d0e0752d2bfd_1_2159586065\\\",\\\"startFailedCode\\\":-1,\\\"startFailedMsg\\\":\\\"\\\",\\\"startQueueTimeStamp\\\":-1,\\\"statusCode\\\":\\\"\\\",\\\"statusMsg\\\":\\\"\\\",\\\"subNetworkType\\\":\\\"wifi\\\",\\\"upLoadIndex\\\":1,\\\"userID\\\":\\\"1567590276721067\\\",\\\"userIp\\\":\\\"172.28.113.65\\\"}\",\"bizExtraInfo\":\"{\\\"anchorId\\\":\\\"2159586065\\\",\\\"liveStreamId\\\":\\\"ks704813344473608051\\\",\\\"opponentUserIds\\\":[]}\",\"appId\":\"ks704813344473608051\",\"sessionId\":\"a681d0e0752d2bfd_1_2159586065\"}"


    val jsonB = "{\"appId\":\"ks707065143182458884\",\"bulletPlayInfo\":\"{\\\"captureAvgTime\\\":0,\\\"userID\\\":\\\"1554024004752343\\\",\\\"connectLostCode\\\":-1,\\\"playEndTime\\\":1690024867587,\\\"renderMaxTime\\\":0,\\\"lossRateAvg\\\":0,\\\"monitorPeriodStartTime\\\":1690024861203,\\\"playStartTime\\\":1690024839006,\\\"playTotalTime\\\":28581,\\\"lossPacketAllCnt\\\":-1,\\\"freshRateAvg\\\":24,\\\"bitRateMax\\\":544,\\\"platform\\\":\\\"Weier\\\",\\\"screenCallBackCnt\\\":-1,\\\"decodeMaxTime\\\":11,\\\"displayChange2SuccessDelay\\\":0,\\\"renderAvgTime\\\":0,\\\"startQueueTimeStamp\\\":-1,\\\"kwaiVersion\\\":\\\"11.6.30\\\",\\\"displayChangeCnt\\\":0,\\\"app\\\":\\\"debug\\\",\\\"lossRateMax\\\":0,\\\"connectLostCnt\\\":0,\\\"sessionID\\\":\\\"d890455689452ead_1_2154864902\\\",\\\"initCloudPlayTime\\\":-1,\\\"display\\\":\\\"1080P\\\",\\\"decodeAvgTime\\\":4,\\\"userIp\\\":\\\"172.28.113.65\\\",\\\"networkTrafficAvg\\\":-1,\\\"networkType\\\":\\\"wifi\\\",\\\"endToEndDelayAvg\\\":26,\\\"sdkVersion\\\":\\\"11.6.30\\\",\\\"click2ScreenTime\\\":-1,\\\"subNetworkType\\\":\\\"wifi\\\",\\\"encodeMaxTime\\\":3,\\\"lossPacketExceptionCnt\\\":-1,\\\"completeQueueTimeStamp\\\":-1,\\\"queueStatus\\\":-1,\\\"monitorPeriodEndTime\\\":1690024867587,\\\"screenOrientation\\\":\\\"portrait\\\",\\\"reconnectCnt\\\":0,\\\"playServerIp\\\":\\\"103.102.203.195\\\",\\\"bitRateAvg\\\":434,\\\"startFailedCode\\\":-1,\\\"encodeAvgTime\\\":2,\\\"queueTime\\\":-1,\\\"packageName\\\":\\\"pangkezhengba\\\",\\\"captureMaxTime\\\":3,\\\"freshRateMin\\\":-1,\\\"firstScreen\\\":-1,\\\"networkTrafficMax\\\":-1,\\\"extraFields\\\":\\\"2154864902\\\",\\\"homeTime\\\":0,\\\"endToEndDelayMax\\\":35}\",\"sessionId\":\"d890455689452ead_1_2154864902\",\"bizExtraInfo\":\"{\\\"anchorId\\\":2154864902,\\\"liveStreamId\\\":\\\"0kKzm0UULCg\\\"}\"}"

//    val list = compareKey(jsonA, jsonB)
//    list.forEach {
//      Log.d("orangeTest", it)
//    }

    compareValue(jsonA, jsonB)
  }


  fun compareValue(jsonA: String, jsonB: String) {
    val objA = parse(JSONObject(jsonA)).get("bulletPlayInfo") as JSONObject
    val objB = parse(JSONObject(jsonB)).get("bulletPlayInfo") as JSONObject
    val compareList = ArrayList<Pair<String, String>>()

    objA.keys().forEach {
      val vA = objA.get(it)?:"null"
      val vB = if (objB.has(it)) {
        objB.get(it)
      } else {
        "null"
      }
      compareList.add(it to "android=$vA  iOS=$vB")

    }

    compareList.forEach {
      Log.d("orangeTest", "compareValue: " + it.first + "   " + it.second)
    }
  }


  fun getValue(key: String, json: JSONObject): Any? {
    if (json.has(key)) {
      return json.get(key)
    }
    json.keys().forEach {
      val v = json.get(it)
      if (v is JSONObject) {
        return getValue(key, v)
      }
    }
    return null
  }


  /**
   * 比较两个json字符串字段是否对齐
   * @param jsonA 数据1
   * @param jsonB 数据2
   */
  fun compareKey(jsonA: String, jsonB: String): ArraySet<String> {
    val objA = parse(JSONObject(jsonA))
    val objB = parse(JSONObject(jsonB))

    val different = ArraySet<String>()
    getKeys(objA).forEach {
      if (!containsKey(it, objB)) {
        different.add("only Android contains  $it")
      }
    }
    getKeys(objB).forEach {
      if ((!containsKey(it, objA))) {
        different.add("only iOS contains $it")
      }
    }
    return different
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

  private fun containsKey(key: String, json: JSONObject): Boolean {
    if (json.has(key)) {
      return true
    }
    json.keys().forEach {
      val obj = json.get(it)
      if (obj is JSONObject) {
        if (containsKey(key, obj)) {
          return true
        }
      }
    }
    return false
  }

  private fun getKeys(json: JSONObject): List<String> {
    val list = ArrayList<String>()
    json.keys().forEach {
      val v = json.get(it)
      if (v is JSONObject) {
        list.addAll(getKeys(v))
      } else {
        list.add(it)
      }
    }
    return list
  }

  private fun isJSONObject(string: String): Boolean {
    return try {
      JSONObject(string)
      true
    } catch (e: Exception) {
      false
    }
  }

  private fun isJsonArray(string: String): Boolean {
    return try {
      JSONArray(string)
      true
    } catch (e: Exception) {
      false
    }
  }
}