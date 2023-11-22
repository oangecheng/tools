package com.orange.zax.dstclient.pages

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.orange.zax.dstclient.AdminAccount
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.pages.BuySkinActivity.Companion.TYPE_CUSTOM
import com.orange.zax.dstclient.pages.BuySkinActivity.Companion.TYPE_KEY
import com.orange.zax.dstclient.pages.BuySkinActivity.Companion.TYPE_SPONSOR
import com.orange.zax.dstclient.utils.DstAlert

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
class DstHomeActivity : DstActivity() {

  override fun getLayoutRes(): Int {
    return R.layout.dst_home_layout
  }

  override fun onBind(data: Bundle?) {

    findViewById<View>(R.id.dst_skin_unlock)
      .onClickFilter {
        val bundle = Bundle().also {
          it.putInt(TYPE_KEY, TYPE_SPONSOR)
        }
        jump(BuySkinActivity::class.java, false, bundle)
      }

    findViewById<View>(R.id.dst_skin_custom_unlock)
      .onClickFilter {
        val bundle = Bundle().also {
          it.putInt(TYPE_KEY, TYPE_CUSTOM)
        }
        jump(BuySkinActivity::class.java, false, bundle)
      }


    val isMaster = TextUtils.equals("orange", AdminAccount.name())
    findViewById<View>(R.id.dst_skin_manage)
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

    findViewById<View>(R.id.dst_skin_user)
      .onClickFilter {
        jump(UserActivity::class.java)
      }

    findViewById<View>(R.id.dst_skin_bill)
      .onClickFilter {
        jump(DstBillActivity::class.java)
      }
  }
}