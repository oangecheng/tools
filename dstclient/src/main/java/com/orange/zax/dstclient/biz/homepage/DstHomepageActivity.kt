package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.view.View
import com.orange.zax.dstclient.BackPressInterceptor
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.biz.homepage.data.ItemCache
import com.orange.zax.dstclient.biz.homepage.data.ItemType
import com.ustc.zax.base.fragment.BaseFragment


/**
 * Time: 2024/5/29
 * Author: chengzhi@kuaishou.com
 */
class DstHomepageActivity : DstActivity() {

  companion object {

    init {
      ItemCache.init()
    }
  }

  override fun getLayoutRes(): Int {
    return R.layout.dst_fragment_container_layout
  }


  override fun onBind(data: Bundle?) {
    findViewById<View>(R.id.manage).onClickFilter {
      change(PageMain.instance())
    }
    findViewById<View>(R.id.prefab).onClickFilter {
      change(PageItem.instance(ItemType.RECIPE))
    }

    findViewById<View>(R.id.image).onClickFilter {
      change(PageItem.instance(ItemType.IMAGE))
    }

    findViewById<View>(R.id.tab).onClickFilter {
      change(PageTabDesc.instance())
    }
  }

  override fun onBackPressed() {
    val frg = supportFragmentManager.fragments
    frg.forEach {
      if (it is BackPressInterceptor) {
        if (it.intercept()) {
          return
        }
      }
    }
    if (supportFragmentManager.backStackEntryCount > 0) {
      supportFragmentManager.popBackStack()
    } else {
      super.onBackPressed()
    }
  }

  private fun change(page: BaseFragment) {
    val id = R.id.container
    supportFragmentManager
      .beginTransaction()
      .replace(id, page)
      .addToBackStack("container")
      .commitAllowingStateLoss()
  }
}


