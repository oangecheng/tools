package com.orange.zax.dstclient.data

/**
 * Time: 2024/5/28
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */

data class ItemInfo(
  var id: String,
  var name: String,
  var desc: String,
  var tabs: MutableList<Int>,
  var tech: Int,
  var gain: String,
  var recipes: MutableList<Recipe>?
) {

  companion object {
    fun mock(): ItemInfo {
      return ItemInfo(
        "", "", "", ArrayList(), 1, "", ArrayList()
      )
    }

    fun copy(from: ItemInfo, to : ItemInfo) {
      to.id = from.id
      to.name = from.name
      to.desc = from.desc
      to.tabs.clear()
      to.tabs.addAll(from.tabs)

      to.recipes?.clear()
      from.recipes?.let { fromList->
        val newList = fromList.map { Recipe(it.id, it.num) }
        to.recipes?.addAll(newList)
      }
    }
  }
}


data class Recipe(
  val id : String,
  val num : Int
)

data class ItemResponse(
  val items : List<ItemInfo>
)