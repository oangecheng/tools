package com.ustc.zax.service.manager

import com.ustc.zax.service.BizService
import com.ustc.zax.service.invocation.obtainDefault

/**
 * Time: 2023/7/22
 * Author: chengzhi@kuaishou.com
 */
class BizServiceCenter : IBizServiceCenter {

  private val services = HashMap<Class<out BizService>, BizService>()

  /**
   * 注册Service
   * @param cls Service的class类型
   * @param def 默认实现
   */
  fun <T:BizService> register(cls: Class<T>, def : BizService) {
    services[cls] = obtainDefault(cls)
    services[cls]?.getProxy()?.setDefaultProxy(def)
  }

  /**
   * 获取Service
   * @param cls Service的class类型
   * 需要提前注册，否则获取会抛异常
   */
  @Suppress("UNCHECKED_CAST")
  override fun <T : BizService> getService(cls: Class<T>): T {
    val service = services[cls]
      ?: throw IllegalAccessException("invalid service, check if the service is registered")
    return service as T
  }
}