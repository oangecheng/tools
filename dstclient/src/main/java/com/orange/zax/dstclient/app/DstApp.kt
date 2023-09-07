package com.orange.zax.dstclient.app

import android.app.Application
import com.ustc.zax.base.GlobalConfig

/**
 * Time: 2023/9/6
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class DstApp : Application() {

  override fun onCreate() {
    super.onCreate()
    GlobalConfig.setApp(this)
  }
}