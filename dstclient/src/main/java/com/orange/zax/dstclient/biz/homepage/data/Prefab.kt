package com.orange.zax.dstclient.biz.homepage.data

import com.google.gson.annotations.SerializedName

/**
 * Time: 2024/12/3
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
data class Prefab(
  @SerializedName("id")
  val id : String,
  @SerializedName("name")
  val name : String,
  @SerializedName("url")
  val url : String?
)