package com.kuaishou.live.viewcontroller

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.IdentityHashMap

/**
 * 负责管理ViewController的生命周期
 * 生命周期调度顺序
 * onCreate -> onStart -> onResume -> onPause -> onStop -> onDestroy
 */
class ViewControllerManagerImpl(
  private val hostLifecycleOwner: LifecycleOwner,
  private val activity: Activity,
  private val context: Context,
  private val viewHost: ViewHost
) : ViewControllerManager {

  constructor(
    hostLifecycleOwner: LifecycleOwner,
    activity: Activity,
    viewHost: ViewHost
  ) : this(hostLifecycleOwner, activity, activity, viewHost)

  private val viewControllersMap = IdentityHashMap<ViewController, LifecycleEventObserver>()

  @MainThread
  override fun addViewController(@IdRes containerId: Int, viewController: ViewController) {
    val container: ViewGroup? = if (containerId == 0) {
      null
    } else {
      viewHost.findViewById(containerId)
        ?: throw IllegalArgumentException("container id " + context.resources.getResourceEntryName(containerId) + " not found")
    }

    addViewControllerInner(container, viewController)
  }

  @MainThread
  override fun addViewController(container: ViewGroup, viewController: ViewController) {
    if (!viewHost.isChildView(container)) {
      throw IllegalStateException("containerView $container is not inside ViewController $this")
    }
    addViewControllerInner(container, viewController)
  }

  private fun addViewControllerInner(container: ViewGroup?, viewController: ViewController) {
    check(hostLifecycleOwner.lifecycle.currentState != Lifecycle.State.INITIALIZED) {
      "addViewController must be called after onCreate"
    }
    check(hostLifecycleOwner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
      "addViewController can not be called after onDestroy"
    }
    check(!viewControllersMap.containsKey(viewController)) {
      "viewController already added [$viewController]"
    }

    val ob = LifecycleEventObserver { _, event ->
      viewController.setCurrentState(event.targetState)
    }

    viewController.attach(this, context, activity, container)
    viewControllersMap[viewController] = ob
    hostLifecycleOwner.lifecycle.addObserver(ob)
    if (viewController.lifecycle.currentState == Lifecycle.State.INITIALIZED) {
      viewController.setCurrentState(Lifecycle.State.CREATED)
    }
  }

  /**
   * 删除viewController，此时会触发ViewController的onDestroy
   * viewController一旦被删除，不能再次添加
   */
  @MainThread
  override fun removeViewController(viewController: ViewController) {
    if (viewController.lifecycle.currentState == Lifecycle.State.DESTROYED) {
      return
    }

    check(viewController.lifecycle.currentState != Lifecycle.State.INITIALIZED) {
      "can not remove $viewController because it has not been added"
    }

    val ob = viewControllersMap.remove(viewController)
    check(ob != null) {
      "ViewController is not added [$viewController]"
    }
    hostLifecycleOwner.lifecycle.removeObserver(ob)
    viewController.setCurrentState(Lifecycle.State.DESTROYED)
  }
}