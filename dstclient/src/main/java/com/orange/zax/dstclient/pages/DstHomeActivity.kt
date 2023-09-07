package com.orange.zax.dstclient.pages

import android.view.View
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.utils.Utils

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
class DstHomeActivity : DstActivity() {

  override fun getLayoutRes(): Int {
    return R.layout.dst_home_layout
  }

  override fun onBizInit() {
    findViewById<View>(R.id.go_buy_skin).setOnClickListener {
      Utils.jumpActivity(this, BuySkinActivity::class.java)
    }

    findViewById<View>(R.id.go_register_skin).setOnClickListener {
      Utils.jumpActivity(this, ManageSkinActivity::class.java)
    }

    findViewById<View>(R.id.go_query_skin).setOnClickListener {
      Utils.jumpActivity(this, UserActivity::class.java)
    }
  }
}