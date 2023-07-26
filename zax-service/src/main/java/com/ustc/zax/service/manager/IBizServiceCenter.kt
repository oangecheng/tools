package com.ustc.zax.service.manager

import com.ustc.zax.service.BizService

/**
 * Time: 2023/7/25
 * Author: chengzhi@kuaishou.com
 */
interface IBizServiceCenter {
  fun <T : BizService> getService(cls: Class<T>): T
}