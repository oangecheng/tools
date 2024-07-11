package com.ustc.zax.service.example

import android.util.Log
import com.ustc.zax.service.BizPresenter

/**
 * Time: 2023/7/25
 * Author: chengzhi@kuaishou.com
 */
class LiveTestBizPresenter1 : BizPresenter() {
  override fun scope(): Int {
    return LiveBizScopes.MAIN
  }

  override fun onBind() {
    super.onBind()
//    getService(LiveTestBizService::class.java).getProxy()?.setBizProxy(
//      object : LiveTestBizService {
//        override fun show(name: String) {
//          Log.d("orangeInvoke", "我被替换了 $name")
//        }
//      })
  }
}