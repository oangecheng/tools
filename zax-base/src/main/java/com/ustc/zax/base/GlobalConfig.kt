package com.ustc.zax.base

import android.app.Application

object GlobalConfig {

  lateinit var application: Application

  fun setApp(app: Application) {
    this.application = app
  }
}