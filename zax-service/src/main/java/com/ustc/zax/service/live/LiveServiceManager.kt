package com.ustc.zax.service.live

import java.lang.reflect.Proxy


/**
 * Time: 2024/7/17
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
class LiveServiceManager {

  private val services = HashMap<Class<*>, LiveService<*>>()

  fun <T : LiveService<T>> register(clz: Class<T>, def: T) {
    services[clz] = proxy(clz, def)
  }

  /**
   * 获取Service
   * @param cls Service的class类型
   * 需要提前注册，否则获取会抛异常
   */
  @Suppress("UNCHECKED_CAST")
  fun <T : LiveService<T>> getService(cls: Class<T>): T {
    val service = services[cls]
    return if (service != null) {
      service as T
    } else {
      throw IllegalAccessException()
    }
  }

  private fun <T : LiveService<T>> proxy(
    clz: Class<T>,
    def: T
  ): T {
    return Proxy.newProxyInstance(
      clz.classLoader,
      arrayOf(clz),
      LiveServiceInvoker.instance(def)
    ) as T
  }
}