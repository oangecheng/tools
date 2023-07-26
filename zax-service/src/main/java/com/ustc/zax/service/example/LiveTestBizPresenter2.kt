package com.ustc.zax.service.example

import com.ustc.zax.service.BizPresenter

/**
 * Time: 2023/7/25
 * Author: chengzhi@kuaishou.com
 */
class LiveTestBizPresenter2 : BizPresenter() {

  override fun scope(): Int {
    return LiveBizScopes.MAIN
  }

  override fun onBind() {
    getService(LiveTestBizService::class.java).show("哈哈")
  }

}