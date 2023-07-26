package com.ustc.zax.service

import androidx.annotation.MainThread
import com.ustc.zax.service.manager.IBizServiceCenter

/**
 * Time: 2023/7/25
 * Author: chengzhi@kuaishou.com
 */
abstract class BizPresenter : BizScope {
  private val subBizList = ArrayList<BizPresenter>()
  private lateinit var serviceCenter: IBizServiceCenter

  @MainThread
  fun addPresenter(subBiz: BizPresenter) {
    if (subBiz.scope() <= scope()) throw IllegalArgumentException("child scope should be larger")
    subBizList.add(subBiz)
  }

  @MainThread
  fun create(center: IBizServiceCenter) {
    serviceCenter = center
    onCreate()
    subBizList.forEach {
      it.create(center)
    }
  }

  @MainThread
  fun bind() {
    onBind()
    subBizList.forEach {
      it.bind()
    }
  }

  @MainThread
  fun unbind() {
    subBizList.forEach {
      it.onUnbind()
    }
    onUnbind()
  }

  @MainThread
  fun destroy() {
    subBizList.forEach {
      it.destroy()
    }
    onDestroy()
  }

  /**
   * get a valid service and return value
   * check the service scope and presenter scope
   * presenter scope should be larger than service scope,
   * otherwise, this method will throw [IllegalArgumentException]
   * @param cls the class of biz service
   */
  fun <T : BizService> getService(cls: Class<T>): T {
    val service = serviceCenter.getService(cls)
    if (service.scope() <= scope()) {
      return service
    } else {
      throw IllegalAccessException("invalid service scope")
    }
  }

  @MainThread
  open fun onCreate() {}
  @MainThread
  open fun onBind() {}
  @MainThread
  open fun onUnbind() {}
  @MainThread
  open fun onDestroy() {}
}