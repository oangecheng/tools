package com.kuaishou.live.viewcontroller.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class SimpleLifecycleOwner: LifecycleOwner {
  private val lifecycleRegistry = LifecycleRegistry(this)

  override fun getLifecycle(): LifecycleRegistry {
    return lifecycleRegistry
  }
}