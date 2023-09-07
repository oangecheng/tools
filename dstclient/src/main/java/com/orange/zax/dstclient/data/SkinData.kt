package com.orange.zax.dstclient.data
import com.google.gson.annotations.SerializedName

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
data class Skin(
  @SerializedName("skinId") val skinId: String,
  @SerializedName("skinName") val skinName: String,
  @SerializedName("prefab") val skinPrefab: String,
  @SerializedName("skinType") val skinType: Int
) {

  @Transient
  var isSelected = false

  override fun toString(): String {
    return "Skin(skinId='$skinId', skinName='$skinName', isSelected=$isSelected)"
  }
}



data class User(
  @SerializedName("userId") val userId : String,
  @SerializedName("skins") val skins : List<Skin>
)


data class SkinListResponse(
  @SerializedName("skins") val skins: List<Skin>?
) {

  override fun toString(): String {
    return "SkinResponse(skins=$skins)"
  }
}


data class UserListResponse(
  @SerializedName("alluser") val users : List<User>
)

