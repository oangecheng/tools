package com.ustc.zax.base

import android.app.Application
import android.content.Context

object GlobalConfig {

  lateinit var context: Context

  fun setApp(app: Application) {
    context = app
  }
}