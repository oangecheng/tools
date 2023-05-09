package com.ustc.orange.tools

import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView

/**
 * Time: 2023/3/10
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class LiveViewResizeLogicT {

  lateinit var list : MutableList<Target<View>>

  fun initView(
    targets : MutableList<Target<View>>
  ) {
    list = targets
    targets.sortWith(Comparator {
        o1, o2 -> o1.priority - o2.priority
    })

    list.forEach {
      Log.d("orangeLog", "initView: " + it.priority)
    }
  }

  fun resize(width: Int) {
    resize(width, list)
  }

  private fun resize(width: Int, targets: MutableList<Target<View>>) {
    var size = 0


    targets.forEach {
      val w = calcWidth(it)
      Log.d("orangeLog", "calcWidth $w")
      size += w
    }



    if (size > width) {
      targets.forEach {
        Log.d("orangeLog", "resize $width  $size  ${it.priority}")

        if (!it.resized) {
          it.resized = it.resize.invoke(it.view, size - width)
          return resize(width, targets)
        }
      }
    }
  }


  private fun calcWidth(target: Target<View>): Int {
    if (target.view.visibility != View.VISIBLE) {
      return 0
    }
    var width = target.view.layoutParams.width
    if (target.view.layoutParams is MarginLayoutParams) {
      val params = target.view.layoutParams as MarginLayoutParams
      width += params.leftMargin + params.rightMargin
    }
    return width
  }
}

data class Target<T : View>(
  val view: T,
  val measureSpec: Pair<Int, Int>,
  val priority: Int,
  var resized : Boolean = false,
  val resize: (self : T, left : Int) -> Boolean
)


class LiveResizeLogicTest(root: View) {

  private val username = root.findViewById<TextView>(R.id.user_name)
  private val mute = root.findViewById<ImageView>(R.id.mute)
  private val follow = root.findViewById<ImageView>(R.id.follow)


  private val logic = LiveViewResizeLogicT()

  init {
    logic.initView(resizeTest())
  }

  fun resize(width: Int) {
    logic.resize(width)
  }



  private fun resizeTest() : MutableList<Target<View>> {
    val t = "我是一个大宝贝"
    val min = MainActivity.getLimitChineseString(t, 2) + "..."
    val max = MainActivity.limitTextByMaxLength(t, 5) + "..."

    val maxW = username.paint.measureText(max).toInt()
    val minW = username.paint.measureText(min).toInt()

    username.layoutParams.width = maxW
    val usernameTarget = Target<TextView>(
      username,
      View.MeasureSpec.UNSPECIFIED to View.MeasureSpec.UNSPECIFIED,
      0
    ) { v, resize ->

      val space = v.layoutParams.width - minW


      Log.d("orangeLog", "resizeTest: text  $resize  $space  $maxW  $minW")

      v as TextView


      if (resize < space) {
        v.layoutParams.width = v.layoutParams.width - resize
        v.requestLayout()
        return@Target false
      } else {
        v.layoutParams.width = v.paint.measureText(min).toInt()
        v.requestLayout()
        return@Target true
      }
    }

    val muteTarget = Target<View>(
      mute,
      View.MeasureSpec.EXACTLY to View.MeasureSpec.EXACTLY,
      2
    ) { v, _ ->
      Log.d("orangeLog", "resizeTest: mute")
      v.visibility = GONE
      return@Target true
    }

    val followTarget = Target<View>(
      follow,
      View.MeasureSpec.EXACTLY to View.MeasureSpec.EXACTLY,
      1
    ) { v, _ ->
      Log.d("orangeLog", "resizeTest: follow")
      v.visibility = GONE
      return@Target true
    }

    val list = ArrayList<Target<View>>()
    list.add(usernameTarget as Target<View>)
    list.add(muteTarget)
    list.add(followTarget)
    return list
  }
}