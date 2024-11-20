package com.kuaishou.live.viewcontroller

import android.view.ViewGroup
import androidx.annotation.IdRes

interface ViewControllerManager {
  fun addViewController(@IdRes containerId: Int, viewController: ViewController)
  fun addViewController(container: ViewGroup, viewController: ViewController)
  fun addViewController(viewController: ViewController) {
    addViewController(0, viewController)
  }
  fun removeViewController(viewController: ViewController)
}