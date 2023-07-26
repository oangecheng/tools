package com.ustc.zax.service.invocation

import com.ustc.zax.service.BizService
import java.lang.reflect.Proxy

/**
 * Time: 2023/7/22
 * Author: chengzhi@kuaishou.com
 */
fun <T : BizService> obtainDefault(service: Class<T>): T {
  return Proxy.newProxyInstance(
    service.classLoader,
    arrayOf(service),
    BizServiceInvokerImpl()
  ) as T
}
