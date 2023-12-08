package com.orange.zax.viewcontroller

import ViewController
import android.view.ViewGroup
import androidx.annotation.IdRes

/**
 * Time: 2023/12/8
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
interface ViewControllerManager {
  fun addViewController(@IdRes containerId: Int, viewController: ViewController)
  fun addViewController(container: ViewGroup, viewController: ViewController)
  fun addViewController(viewController: ViewController) {
    addViewController(0, viewController)
  }
  fun removeViewController(viewController: ViewController)
}

class ManagerImpl : ViewControllerManager {

}