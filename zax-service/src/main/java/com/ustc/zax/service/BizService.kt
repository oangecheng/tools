package com.ustc.zax.service

import com.ustc.zax.service.invocation.BizServiceInvoker

/**
 * Time: 2023/7/22
 * Author: orange
 * Base service for biz
 */
interface BizService {

  fun create() : BizService

  /**
   * set delegate
   * Implementations need to set this proxy.
   * At the same time, we need to pay attention to whether the default return value affects the caller
   */
  @JvmDefault
  fun getProxy(): BizServiceInvoker? {
    return null
  }
}