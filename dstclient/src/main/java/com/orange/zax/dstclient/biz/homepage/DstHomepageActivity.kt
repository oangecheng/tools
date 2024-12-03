package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.view.View
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.ustc.zax.base.fragment.BaseFragment


/**
 * Time: 2024/5/29
 * Author: chengzhi@kuaishou.com
 */
class DstHomepageActivity : DstActivity() {

  override fun getLayoutRes(): Int {
    return R.layout.dst_fragment_container_layout
  }


  override fun onBind(data: Bundle?) {
    findViewById<View>(R.id.add).onClickFilter {
      change(PageAdd.instance())
    }
    findViewById<View>(R.id.update).onClickFilter {
      change(PageUpdate.instance())
    }
  }

  override fun onBackPressed() {
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


