package com.ustc.zax.service.example

import com.ustc.zax.service.BizService

/**
 * Time: 2023/7/25
 * Author: chengzhi@kuaishou.com
 */
interface LiveBizService: BizService {

  @LiveBizScopes
  override fun scope(): Int
}