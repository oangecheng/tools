package com.orange.zax.dstclient.biz.homepage.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.api.XGson
import com.orange.zax.dstclient.biz.homepage.ItemData
import com.orange.zax.dstclient.biz.homepage.ItemInfo
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
  private val NORMAL = HashMap<String, ItemData>()
  private val NORMAL_LIVE = MutableLiveData<List<ItemData>>()

  fun init() {
    request(ItemType.RECIPE)
    request(ItemType.IMAGE)
    normal()
  }

  fun remove(type: Int, id: String) {
    if (type == ItemType.NORMAL) {
      NORMAL.remove(id)
      NORMAL_LIVE.value = NORMAL.map { it.value }
    } else {
      ITEMS[type]?.remove(id)
    }
  }

  fun items(type: Int): List<Prefab> {
    return ITEMS[type]?.map { it.value } ?: emptyList()
  }

  fun item(type: Int, id: String): Prefab? {
    return ITEMS[type]?.get(id)
  }

  fun observeNormal(owner: LifecycleOwner, observer: Observer<List<ItemData>>) {
    NORMAL_LIVE.observe(owner, observer)
  }

  fun normals(): List<ItemData> {
    return NORMAL.map { it.value }
  }

  private fun normal() {
    val d = PageApiService.get()
      .typeItems(ItemType.NORMAL)
      .map(ResponseFunction())
      .map { resp -> resp.items.map { XGson.parse(it.data, ItemData::class.java)!! } }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        it.forEach { i ->
          NORMAL[i.id] = i
        }
        NORMAL_LIVE.value = it
      }, {
        ErrorConsumer().accept(it)
      })
  }

  fun cache(type: Int, item : Prefab) {
    ITEMS[type]?.put(item.id, item)
  }

  fun cache(itemData: ItemData) {
    NORMAL[itemData.id] = itemData
    NORMAL_LIVE.value = NORMAL.map { it.value }
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