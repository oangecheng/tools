package com.orange.zax.dstclient.api

import com.google.gson.annotations.SerializedName

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
data class Response<T>(
  @SerializedName("status") val status: Int,
  @SerializedName("data") val data: T?,
  @SerializedName("msg") val msg: String?
)

data class ActionResponse(
  val status: Int
)