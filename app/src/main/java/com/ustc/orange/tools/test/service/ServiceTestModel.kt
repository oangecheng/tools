package com.ustc.orange.tools.test.service

import android.content.Context
import androidx.lifecycle.Observer
import com.ustc.orange.tools.test.XTestModel
import com.ustc.zax.service.live.LiveService
import com.ustc.zax.service.live.LiveServiceAction
import com.ustc.zax.service.live.LiveServiceManager
import com.ustc.zax.tool.Utils
import com.ustc.zax.tool.XLog

/**
 * Time: 2024/7/18
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
private const val TAG = "LiveServiceTest"

class ServiceTestModel : XTestModel {

  private val serviceMgr = LiveServiceManager()

  init {
    serviceMgr.register(LiveTestService::class.java, LiveTestService.def())
  }

  override fun onStart(context: Context) {
    val service = serviceMgr.getService(LiveTestService::class.java)

    service.printInfo("测试代码")
    service.observe(Observer {
      it.printInfo("service被加载了")
    })
    service.addListener(object : LiveTestListener {
      override fun onStartBiz(biz: String) {
        XLog.d(TAG, "onStartBiz", "biz" to biz)
      }
    })

    val delegate = LiveTestServiceImpl()
    Utils.delayTask(3000L, this, Runnable {
      service.setDelegate(delegate)
      delegate.start()
    })
  }

  override fun onStop() {
    Utils.cancelTask(this)
  }
}


private interface LiveTestService : LiveService<LiveTestService> {

  companion object {
    fun def() : LiveTestService {
      return object : LiveTestService {}
    }
  }

  fun printInfo(info: String) {
    XLog.d(TAG, "default onInfo", "info" to info)
  }

  fun getUsername() : String? {
    XLog.d(TAG, "default getUsername return null")
    return null
  }

  @LiveServiceAction
  fun addListener(listener: LiveTestListener) {
    XLog.d(TAG, "default add Listener", "listener" to  listener)
  }

  fun removeListener(listener: LiveTestListener) {
    XLog.d(TAG, "default remove Listener", "listener" to  listener)
  }
}


private interface LiveTestListener {
  fun onStartBiz(biz : String)
}


private class LiveTestServiceImpl : LiveTestService {

  private val listeners = ArrayList<LiveTestListener>()

  fun start() {
    listeners.forEach {
      it.onStartBiz("pk")
    }
  }

  override fun printInfo(info: String) {
    XLog.d(TAG, "real printInfo", "info" to info)
  }

  override fun getUsername(): String {
    return "orange"
  }

  override fun addListener(listener: LiveTestListener) {
    listeners.add(listener)
  }

  override fun removeListener(listener: LiveTestListener) {
    listeners.remove(listener)
  }
}