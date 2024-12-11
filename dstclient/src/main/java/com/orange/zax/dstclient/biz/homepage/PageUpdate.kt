package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
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
class PageUpdate : PageBase() {

  companion object {
    fun instance(data : ItemData) : BaseFragment {
      return PageUpdate().apply {
        itemInfoFromServer = data
      }
    }
  }

  private var itemInfoFromServer: ItemData? = null

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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btnAction.text = "更新物品"
    itemInfoFromServer?.let {
      ItemData.copy(it, itemInfoCache)
      updatePage(itemInfoCache)
    }
  }

  override fun title(): String {
    return "更新物品"
  }

  override fun onAction(data: ItemData) {
    if (itemInfoFromServer != null) {
      autoDispose(updateItem(data))
    }
  }

  private fun updateItem(data: ItemData): Disposable {
    return PageApiService.get()
      .updateItem(data.id, Gson().toJson(data), ItemType.NORMAL)
      .observeOn(AndroidSchedulers.mainThread())
      .map(ResponseFunction())
      .doOnError(ErrorConsumer())
      .subscribe ({
        ToastUtil.showShort(
          "更新成功"
        )
      }, {
        ErrorConsumer().accept(it)
      })
  }
}