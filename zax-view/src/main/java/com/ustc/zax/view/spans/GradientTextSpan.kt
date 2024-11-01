package com.ustc.zax.view.spans

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader.TileMode
import android.text.TextPaint

/**
 * Time: 2024/11/1
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class GradientTextSpan {

  fun create(string: String): Bitmap {
    val paint = TextPaint().apply {
      textSize = 100f
    }
    val metricsInt = paint.getFontMetricsInt()

    val with = paint.measureText(string).toInt()
    val height = metricsInt.descent - metricsInt.ascent
    paint.shader =
      LinearGradient(0f, 0f, with.toFloat(), 0f, Color.RED, Color.GREEN, TileMode.CLAMP)

    val bitmap = Bitmap.createBitmap(with, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val x = 0f
    val y = -metricsInt.ascent
    canvas.drawColor(Color.YELLOW)
    canvas.drawText(string, x, y.toFloat(), paint)
    return bitmap
  }
}