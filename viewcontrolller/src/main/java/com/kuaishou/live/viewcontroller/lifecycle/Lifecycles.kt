@file:JvmName("LifecyclesExt")
package com.kuaishou.live.viewcontroller.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry


fun merge(owner1: LifecycleOwner, owner2: LifecycleOwner): LifecycleOwner {
  return merge(owner1, owner2, *emptyArray())
}

/**
 * 生命周期合并工具类
 */
fun merge(owner1: LifecycleOwner, owner2: LifecycleOwner, vararg others: LifecycleOwner): LifecycleOwner {
  return object : LifecycleOwner {
    private val owners = listOf(owner1, owner2, *others)
    private val registry = LifecycleRegistry(this)
    private val observer = LifecycleEventObserver { _, _ -> mergeState() }

    init {
      owners.forEach { it.lifecycle.addObserver(observer) }
    }

    private fun mergeState() {
      var state = Lifecycle.State.RESUMED
      owners.forEach {
        state = state.coerceAtMost(it.lifecycle.currentState)
      }

      if (state == Lifecycle.State.DESTROYED) {
        owners.forEach { it.lifecycle.removeObserver(observer) }
      }

      if (registry.currentState == Lifecycle.State.INITIALIZED
        && state == Lifecycle.State.DESTROYED
      ) {

      } else {
        registry.currentState = state
      }
    }

    override fun getLifecycle(): Lifecycle {
      return registry
    }
  }
}