package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.biz.homepage.data.ItemCache
import com.orange.zax.dstclient.biz.homepage.data.ItemType
import com.orange.zax.dstclient.utils.ToastUtil
import com.ustc.zax.base.fragment.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * Time: 2024/12/2
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class PageAdd private constructor() : PageBase() {

  companion object {
    fun instance() : BaseFragment {
      return PageAdd()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(
      R.layout.dst_homepage_layout,
      container,
      false
    )
  }

  override fun title(): String {
    return "新增物品"
  }

  override fun onAction(data: ItemData) {
    autoDispose(addNewItem(data))
  }

  private fun addNewItem(data: ItemData): Disposable {
    return PageApiService.get()
      .addItem(data.id, Gson().toJson(data), ItemType.NORMAL)
      .observeOn(AndroidSchedulers.mainThread())
      .map(ResponseFunction())
      .subscribe({
        ItemCache.cache(data)
        ToastUtil.showShort("新增物品成功")
      }, {
        ErrorConsumer().accept(it)
      })
  }
}