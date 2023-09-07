package com.orange.zax.dstclient

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.orange.zax.dstclient.pages.BuySkinActivity
import com.orange.zax.dstclient.pages.ManageSkinActivity
import com.orange.zax.dstclient.pages.UserActivity
import com.orange.zax.dstclient.utils.Utils

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
class DstHomeActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dst_home_layout)

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