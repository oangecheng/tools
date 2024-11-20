@file:JvmName("LifecycleExt")
package com.kuaishou.live.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

fun LifecycleOwner.doOnDestroy(action: () -> Unit) {
  doOnNextLifecycle(Lifecycle.State.DESTROYED, action)
}

fun <T : Any> LifecycleOwner.doOnDestroy(weakRef: T, action: (T) -> Unit) {
  val ref = WeakReference(weakRef)

  val listener = object : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
      if (ref.get() == null) {
        // 资源已经释放了，没必要继续监听
        source.lifecycle.removeObserver(this)
      } else if (event == Lifecycle.Event.ON_DESTROY) {
        ref.get()?.let { action(it) }
        source.lifecycle.removeObserver(this)
      }
    }
  }

  lifecycle.addObserver(listener)
}

fun LifecycleOwner.doOnNextLifecycle(state: Lifecycle.State, action: () -> Unit) {
  lifecycle.addObserver(object : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
      if (event.targetState == state) {
        action()
        source.lifecycle.removeObserver(this)
      }
    }
  })
}
