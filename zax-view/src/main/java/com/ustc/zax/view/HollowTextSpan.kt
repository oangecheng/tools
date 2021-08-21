package com.ustc.zax.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.style.ReplacementSpan
import androidx.annotation.IntDef
import androidx.annotation.Px

/**
 * 可以实现镂空效果的自定义ImageSpan
 * 默认居中显示，需要其他属性，自行扩展接口
 * span的位置逻辑copy自fresco的BetterImageSpan
 * @property text 文字
 * @property bg 背景
 */
class HollowTextSpan(private val text: String, private val bg: Drawable) : ReplacementSpan() {

  private var width = 0
  private var height = 0
  @SpanAlignment
  private var alignment = ALIGN_CENTER

  private val fontMetricsInt = Paint.FontMetricsInt()
  private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val textArea = Rect()
  private val canvasArea = RectF()

  init {
    // 镂空效果实现的关键
    textPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    textPaint.textSize = 30f
    textPaint.color = Color.BLACK
    updateTextArea()
  }

  /**
   * 设置文字大小
   * 设置大小之后需要重新计算下文字的区域
   * @param sizePx 像素值
   */
  fun setTextSize(@Px sizePx: Float) {
    textPaint.textSize = sizePx
    updateTextArea()
  }

  override fun getSize(
      paint: Paint, text: CharSequence?,
      start: Int, end: Int,
      fontMetrics: Paint.FontMetricsInt?
  ): Int {
    updateBounds()
    if (fontMetrics == null) {
      return width
    }
    val offsetAbove = getOffsetAboveBaseline(fontMetrics)
    val offsetBelow = height + offsetAbove
    if (offsetAbove < fontMetrics.ascent) {
      fontMetrics.ascent = offsetAbove
    }
    if (offsetAbove < fontMetrics.top) {
      fontMetrics.top = offsetAbove
    }
    if (offsetBelow > fontMetrics.descent) {
      fontMetrics.descent = offsetBelow
    }
    if (offsetBelow > fontMetrics.bottom) {
      fontMetrics.bottom = offsetBelow
    }
    return width
  }

  override fun draw(
      canvas: Canvas, chs: CharSequence?,
      start: Int, end: Int,
      x: Float, top: Int, y: Int, bottom: Int,
      paint: Paint
  ) {

    // 更新画布区域，缓存
    canvasArea.right = canvas.width.toFloat()
    canvasArea.bottom = canvas.height.toFloat()
    val sc = canvas.saveLayer(canvasArea, null)

    // 绘制背景
    paint.getFontMetricsInt(fontMetricsInt)
    val iconTop = y + getOffsetAboveBaseline(fontMetricsInt)
    canvas.translate(x, iconTop.toFloat())
    bg.draw(canvas)
    canvas.translate(-x, -iconTop.toFloat())

    // 绘制文字，计算文字的xy坐标
    val textX = bg.bounds.centerX() - textArea.centerX() + x
    val textY = bg.bounds.centerY() - textArea.centerY() + iconTop
    canvas.drawText(text, textX, textY.toFloat(), textPaint)

    // 恢复canvas
    canvas.restoreToCount(sc)
  }

  private fun updateBounds() {
    width = bg.bounds.width()
    height = bg.bounds.height()
  }

  private fun updateTextArea() {
    textPaint.getTextBounds(text, 0, text.length, textArea)
  }

  private fun getOffsetAboveBaseline(fm: Paint.FontMetricsInt): Int {
    return when (alignment) {
      ALIGN_BOTTOM -> fm.descent - height
      ALIGN_CENTER -> {
        val textHeight = fm.descent - fm.ascent
        val offset = (textHeight - height) / 2
        fm.ascent + offset
      }
      ALIGN_BASELINE -> -height
      else -> -height
    }
  }

  /**
   * span在整个富文本当中的位置
   * [ALIGN_BASELINE] 居于基准线
   * [ALIGN_CENTER] 居中显示
   * [ALIGN_BOTTOM] 居于底部显示
   */
  @IntDef(ALIGN_BASELINE, ALIGN_BOTTOM, ALIGN_CENTER)
  @Retention(AnnotationRetention.SOURCE)
  annotation class SpanAlignment

  companion object {
    const val ALIGN_BOTTOM = 0
    const val ALIGN_BASELINE = 1
    const val ALIGN_CENTER = 2
  }
}