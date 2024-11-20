package com.kuaishou.live.viewcontroller

import android.view.View

interface ViewHost {
  fun <T : View> findViewById(resId: Int): T?

  fun isChildView(view: View): Boolean

  companion object {
    @JvmStatic
    fun forViewProvider(viewProvider: () -> View?): ViewHost {
      return SimpleViewHost(viewProvider)
    }

    @JvmStatic
    fun forView(view: View): ViewHost {
      return SimpleViewHost { view }
    }

    @JvmStatic
    fun forNoView(): ViewHost {
      return SimpleViewHost { null }
    }

    @JvmStatic
    fun isChildView(container: View, child: View): Boolean {
      var v: View? = child
      while (v != null) {
        if (v === container) {
          return true
        }
        v = v.parent as? View
      }
      return false
    }
  }
}

private class SimpleViewHost(private val viewProvider: () -> View?) : ViewHost {
  override fun <T : View> findViewById(resId: Int): T? {
    return viewProvider()?.findViewById(resId)
  }

  override fun isChildView(view: View): Boolean {
    val container = viewProvider() ?: return false
    return ViewHost.isChildView(container, view)
  }
}
