package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.api.XGson
import com.orange.zax.dstclient.biz.homepage.data.ItemCache
import com.orange.zax.dstclient.biz.homepage.data.ItemType
import com.orange.zax.dstclient.biz.homepage.data.TabInfo
import com.orange.zax.dstclient.utils.ToastUtil
import com.ustc.zax.base.fragment.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2024/12/25
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class PageTabDesc : BaseFragment() {
  companion object {
    private const val TAB_ID = "tab_info_"

    fun instance() : BaseFragment {
      return PageTabDesc()
    }
  }



  private var tab = 0

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(
      R.layout.dst_homepage_tabinfo,
      container,
      false
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val desc = findViewById<EditText>(R.id.tab_info)


    val select = findViewById<Button>(R.id.tab_select)
    select.setOnClickListener {
      HomeTabSelectDialog.newInstance(emptyList(), object : HomeTabSelectDialog.Listener {
        override fun onDismiss(tabs: List<Int>) {
          if (tabs.size == 1) {
            tab = tabs[0]
            select.text = "已选择：${TABS[tab]}"
          }
        }
      }).show(
        childFragmentManager,
        "tab"
      )
    }

    findViewById<View>(R.id.tab_search).setOnClickListener {
      val id = "${TAB_ID}${tab}"
      val da = ItemCache.tab(id)
      if (da != null) {
        desc.setText(da.desc)
      }
    }

    findViewById<View>(R.id.tab_update).setOnClickListener {
      if (tab == 0) return@setOnClickListener
      val d = desc.text.toString()
      if (d.isEmpty()) {
        return@setOnClickListener
      }
      val id = "${TAB_ID}${tab}"

      val info = TabInfo(id, tab, d)
      val str = XGson.GSON.toJson(info)

      PageApiService.get().updateItem(id, str, ItemType.TAB)
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          ItemCache.cacheTab(id, info)
          ToastUtil.showShort("更新成功")
        }, {

        }).also {
          autoDispose(it)
        }
    }

    findViewById<View>(R.id.tab_add).setOnClickListener {
      if (tab == 0) return@setOnClickListener
      val d = desc.text.toString()
      if (d.isEmpty()) {
        return@setOnClickListener
      }
      val id = "${TAB_ID}${tab}"

      val info = TabInfo(id, tab, d)
      val str = XGson.GSON.toJson(info)

      PageApiService.get().addItem(id, str, ItemType.TAB)
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          ItemCache.cacheTab(id, info)
          ToastUtil.showShort("新增成功")
        }, {

        }).also {
          autoDispose(it)
        }
    }

    findViewById<View>(R.id.tab_del).setOnClickListener {
      if (tab == 0) return@setOnClickListener
      val d = desc.text.toString()
      if (d.isEmpty()) {
        return@setOnClickListener
      }
      val id = "${TAB_ID}${tab}"
      PageApiService.get().deleteItem(id)
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          ItemCache.cacheTab(id, null)
          ToastUtil.showShort("删除成功")
        }, {

        }).also {
          autoDispose(it)
        }
    }

  }
}