package com.ustc.zax.base.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
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

  fun sp2px(sp: Float): Int {
    val fontScale = res().displayMetrics.scaledDensity
    return (sp * fontScale + 0.5f).toInt()
  }

  fun sp2px(sp: Int): Int {
    return sp2px(sp.toFloat())
  }

  fun dimen(@DimenRes dimenRes: Int): Int {
    return res().getDimension(dimenRes).toInt()
  }

  fun drawable(@DrawableRes drawableRes: Int): Drawable? {
    return ContextCompat.getDrawable(context(), drawableRes);
  }

  @ColorInt
  fun color(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(context(), colorRes)
  }

  fun string(@StringRes stringRes: Int): String {
    return res().getString(stringRes)
  }

  fun res(): Resources {
    return context().resources
  }

  fun activity(view: View): Activity? {
    var context = view.context
    while (context is ContextWrapper) {
      if (context is Activity) {
        return context
      }
      context = context.baseContext
    }
    return null
  }

  private fun context(): Context {
    return GlobalConfig.application
  }
}