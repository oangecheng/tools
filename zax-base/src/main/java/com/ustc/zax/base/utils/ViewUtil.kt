package com.ustc.zax.base.utils

import android.content.Context
import android.content.res.Resources
import com.ustc.zax.base.GlobalConfig

/**
 * View相关工具类
 */
object ViewUtil {

  /**
   * dp值转px值
   * @param dp float
   * @return px值
   */
  fun dp2px(dp: Float): Int {
    val scale = res().displayMetrics.density
    return (dp * scale + 0.5f).toInt()
  }

  fun dp2px(dp: Int): Int {
    return dp2px(dp.toFloat())
  }

  fun res(): Resources {
    return context().resources
  }

  private fun context(): Context {
    return GlobalConfig.context
  }
}