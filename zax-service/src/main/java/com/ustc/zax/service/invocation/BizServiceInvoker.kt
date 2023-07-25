package com.ustc.zax.service.invocation

import com.ustc.zax.service.BizService
import java.lang.reflect.InvocationHandler

/**
 * Time: 2023/7/22
 * Author: chengzhi@kuaishou.com
 */
interface BizServiceInvoker : InvocationHandler {
  fun setBizProxy(proxy: BizService)
  fun setDefaultProxy(defaultProxy: BizService)
}