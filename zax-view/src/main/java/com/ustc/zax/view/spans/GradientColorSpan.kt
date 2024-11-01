package com.ustc.zax.view.spans


import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance

/**
 * Time: 2024/10/31
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc: 渐变文字，换行渐变不连续，可以在不换行的场景下使用
 */
class GradientColorSpan(
  private val str : String,
  private val startColor: Int,
  private val endColor: Int
) : CharacterStyle(), UpdateAppearance {

  override fun updateDrawState(tp: TextPaint) {
    val with = tp.measureText(str)
    tp.shader = LinearGradient(0f, 0f, with, 0f, startColor, endColor, Shader.TileMode.CLAMP)
  }
}