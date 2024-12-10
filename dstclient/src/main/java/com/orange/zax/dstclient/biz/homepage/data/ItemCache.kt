package com.orange.zax.dstclient.biz.homepage.data

import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.api.XGson
import com.orange.zax.dstclient.biz.homepage.PageApiService
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2024/12/10
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc: 除了type=1的
 */
object ItemCache {
  private val ITEMS = HashMap<Int, MutableMap<String, Prefab>>()

  fun init() {
    request(ItemType.RECIPE)
    request(ItemType.IMAGE)
  }

  fun items(type: Int): List<Prefab> {
    return ITEMS[type]?.map { it.value } ?: emptyList()
  }

  fun item(type: Int, id: String): Prefab? {
    return ITEMS[type]?.get(id)
  }

  fun cache(type: Int, item : Prefab) {
    ITEMS[type]?.put(item.id, item)
  }

  private fun request(type: Int) {
    val d = PageApiService.get().typeItems(type)
      .map(ResponseFunction())
      .map { resp ->
        resp.items.map {
          XGson.parse(it.data, Prefab::class.java)!!
        }
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        val data = HashMap<String, Prefab>()
        it.forEach { i->
          data[i.id] = i
        }
        ITEMS[type] = data
      }
  }
}