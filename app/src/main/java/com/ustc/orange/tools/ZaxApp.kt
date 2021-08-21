package com.ustc.orange.tools

import android.app.Application
import com.ustc.zax.base.GlobalConfig

class ZaxApp:Application() {

  override fun onCreate() {
    super.onCreate()
    GlobalConfig.setApp(this)
  }
}