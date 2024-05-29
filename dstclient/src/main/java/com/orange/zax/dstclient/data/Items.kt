package com.orange.zax.dstclient.data

/**
 * Time: 2024/5/28
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */

data class ItemInfo(
  val id: String,
  val name: String,
  val desc: String,
  val tabs: List<Int>,
  val tech: Int,
  val gain: String,
  val recipes: List<Recipe>?
)


data class Recipe(
  val id : String,
  val num : Int
)

data class ItemResponse(
  val items : List<ItemInfo>
)