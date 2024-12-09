package com.orange.zax.dstclient.biz.homepage

import com.google.gson.annotations.SerializedName

/**
 * Time: 2024/5/28
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */

data class ItemInfo(
  @SerializedName("id")
  var id: String,
  @SerializedName("type")
  val type: Int,
  @SerializedName("data")
  val data: String
)


data class ItemData(
  @SerializedName("id")
  var id: String,
  @SerializedName("name")
  var name: String,
  @SerializedName("image")
  var image : String,
  @SerializedName("desc")
  var desc: String,
  @SerializedName("tabs")
  var tabs: MutableList<Int>,
  @SerializedName("gainType")
  var gainType: Int,
  @SerializedName("gain")
  var gain: String,
  @SerializedName("recipes")
  var recipes: MutableList<Recipe>?
) {

  companion object {
    fun mock() : ItemData {
      return ItemData(
        "", "", "", "", ArrayList(), 1, "", ArrayList()
      )
    }

    fun copy(from: ItemData, to: ItemData) {
      to.id = from.id
      to.name = from.name
      to.desc = from.desc
      to.tabs.clear()
      to.tabs.addAll(from.tabs)
      to.image = from.image
      to.gain = from.gain

      to.recipes?.clear()
      from.recipes?.let { fromList ->
        val newList = fromList.map { Recipe(it.id, it.num, it.url) }
        to.recipes?.addAll(newList)
      }
    }
  }
}


data class Recipe(
  @SerializedName("id")
  val id : String,
  @SerializedName("num")
  val num : Int,
  @SerializedName("url")
  val url : String?
)

data class ItemResponse(
  @SerializedName("pageData")
  val items : List<ItemInfo>
)
