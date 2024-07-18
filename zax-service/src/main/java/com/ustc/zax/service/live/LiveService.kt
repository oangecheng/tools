package com.ustc.zax.service.live

import androidx.annotation.Keep
import androidx.lifecycle.Observer

/**
 * Time: 2024/7/17
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
@Keep
interface LiveService<T> {

  @JvmDefault
  fun setDelegate(service: T) {}

  @JvmDefault
  fun observe(observer: Observer<T>) {}

  @JvmDefault
  fun isAvailable(): Boolean {
    return false
  }
}