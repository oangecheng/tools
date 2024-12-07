package com.orange.zax.dstclient.biz.homepage.data

import com.google.gson.annotations.SerializedName

/**
 * Time: 2024/12/6
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
data class ImageResponse(
  @SerializedName("image")
  val image: Image
)

data class Image(
  @SerializedName("url")
  val url : String
)
