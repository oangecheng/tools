package com.ustc.orange.tools

import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView

/**
 * Time: 2023/3/14
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class LiveResizeLogic {

  private val logics = ArrayList<Logic<View>>()
  private val comparator = Comparator<Logic<*>> {
      o1, o2 ->  o1.priority - o2.priority
  }

  /**
   * 添加压缩逻辑
   * @param logic 逻辑单元
   */
  @Suppress("UNCHECKED_CAST")
  fun <T:View> addLogic(logic: Logic<T>) {
    logics.add(logic as Logic<View>)
    map[logic] = false
    logics.sortWith(comparator)
  }

  private val map = HashMap<Logic<*>, Boolean>()

  /**
   * 开始resize
   * @param requireWidth 业务要求的宽度
   */
  fun resize(requireWidth : Int) {
    var width = 0

    var finish = true
    map.forEach {
      Log.d("orangeLog", "map: ${it.value}")
      finish = it.value && finish
    }
    if (finish) {
      map.forEach {
        map[it.key] = false
      }
      return
    }

    logics.forEach {
      width += calculateOccupyWidth(it.view)
    }

    logics.forEach {
      if (map[it] != true) {
        val size = width - requireWidth
        Log.d("orangeLog", "resize: $width  $requireWidth")
        map[it] = it.resize(it.view, size)
        it.view.requestLayout()
        return resize(requireWidth)
      }
    }

  }

  private fun calculateOccupyWidth(view: View): Int {
    if (view.visibility != View.VISIBLE) return 0
    var width = view.layoutParams.width
    if (view.layoutParams is MarginLayoutParams) {
      val params = view.layoutParams as MarginLayoutParams
      width += params.leftMargin + params.rightMargin
    }
    return width
  }
}


/**
 * 压缩对象
 * @param view 被作用的view
 * @param priority 优先级，值越小，优先执行压缩逻辑
 * @param resize(v, size) -> ret v当前view， size需要压缩的尺寸， return 是否完成压缩，支持多次压缩
 */
data class Logic<T : View>(
  val view: T,
  val priority: Int,
  val resize: (v: T, size: Int) -> Boolean
)


fun create(username: TextView): Logic<TextView> {
  val text = username.text.toString()
  val maxWidth = username.paint.measureText(
    MainActivity.getLimitChineseString(text, 5) + "..."
  ).toInt()
  val minWidth = username.paint.measureText(
    MainActivity.getLimitChineseString(text, 2) + "..."
  ).toInt()
  username.layoutParams.width = maxWidth
  return Logic(
    view = username,
    priority = 0
  ) { v, size ->
    if (size <= 0) return@Logic true
    val canResizeSpace = v.layoutParams.width - minWidth
    Log.d("orangeLog", "resize text size=$size  canResize=$canResizeSpace")
    if (size < canResizeSpace) {
      v.layoutParams.width -= size
      return@Logic false
    } else {
      v.layoutParams.width = minWidth
      return@Logic true
    }

  }
}


class LiveResizeLogicDemo(root : View) {
  private val username = root.findViewById<TextView>(R.id.user_name)
  private val mute = root.findViewById<ImageView>(R.id.mute)
  private val follow = root.findViewById<ImageView>(R.id.follow)

  private val logic = LiveResizeLogic()

  init {

    logic.addLogic(create(username))
    logic.addLogic(Logic<View>(
      view = follow,
      priority = 1
    ) { v, size ->
      if (size <= 0) return@Logic true
      v.visibility = View.GONE
      return@Logic true
    })

    logic.addLogic(Logic<View>(
      view = mute,
      priority = 2
    ) { v, size ->
      if (size <= 0) return@Logic true
      v.visibility = View.GONE
      return@Logic true
    })

  }

  fun startResize(width : Int) {
    mute.visibility = View.VISIBLE
    follow.visibility = View.VISIBLE
    logic.resize(width)
  }
}