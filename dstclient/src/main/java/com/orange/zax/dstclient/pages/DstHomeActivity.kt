package com.orange.zax.dstclient.pages

import android.text.TextUtils
import android.view.View
import com.orange.zax.dstclient.AdminAccount
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.utils.DstAlert
import com.orange.zax.dstclient.utils.Utils

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
class DstHomeActivity : DstActivity() {

  override fun getLayoutRes(): Int {
    return R.layout.dst_home_layout
  }

  override fun onBind() {
    findViewById<View>(R.id.go_buy_skin)
      .onClickFilter {
        jump(BuySkinActivity::class.java)
      }


    val isMaster = TextUtils.equals("orange", AdminAccount.name())
    findViewById<View>(R.id.go_register_skin)
      .also {
        it.visibility = if (isMaster) View.VISIBLE else View.GONE
      }
      .onClickFilter {
        if (isMaster) {
          jump(ManageSkinActivity::class.java)
        } else {
          DstAlert.alert(this, "暂无权限使用")
        }
      }

    findViewById<View>(R.id.go_query_skin)
      .onClickFilter {
        jump(UserActivity::class.java)
      }
  }
}