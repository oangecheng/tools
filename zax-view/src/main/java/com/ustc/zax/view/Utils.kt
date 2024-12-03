package com.ustc.zax.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader.TileMode
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.Px

/**
 * Time: 2024/12/3
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class Utils {

  companion object {
    /**
     * FUNC_DES: 创建一个渐变色文本的图片
     * @param sizePx 文本字体大小，px值
     * @param text 文本内容
     * @return bitmap对象，注意不要大量创建，可以缓存一下
     * 色值可自己改成数组形式，自行替换
     */
    fun newGradientTextBitmap(
      text: String,
      @Px sizePx: Float,
      @ColorInt startColor: Int,
      @ColorInt endColor: Int
    ): Bitmap {
      val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textSize = sizePx
      }
      val metricsInt = paint.getFontMetricsInt()
      val with = paint.measureText(text).toInt().coerceAtLeast(1)
      val height = (metricsInt.descent - metricsInt.ascent).coerceAtLeast(1)
      Log.i("Utils", "newGradientTextBitmap str=$text, size=($with $height)")
      paint.shader =
        LinearGradient(0f, 0f, with.toFloat(), 0f, startColor, endColor, TileMode.CLAMP)
      val bitmap = Bitmap.createBitmap(with, height, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(bitmap)
      val x = 0f
      val y = -metricsInt.ascent
      canvas.drawText(text, x, y.toFloat(), paint)
      return bitmap
    }
  }
}