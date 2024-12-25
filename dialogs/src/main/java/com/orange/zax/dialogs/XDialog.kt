package com.orange.zax.dialogs

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.ustc.zax.base.utils.ViewUtil
import com.ustc.zax.dialogs.R

/**
 * Time: 2024/6/7
 * Author: chengzhi@kuaishou.com
 */
abstract class XDialog : DialogFragment() {

  companion object {
    private val HEIGHT = ViewUtil.dp2px(438f)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val root = inflater.inflate(
      R.layout.dialog_layout,
      container,
      false
    )


    root.setBackgroundResource(bg())
    getTitle()?.let {
      root.findViewById<TextView>(R.id.title).text = it
    }

    root.findViewById<FrameLayout>(
      R.id.content_container
    ).addView(
      getLayout(inflater, container),
      lp()
    )

    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_Translucent)
    dialog?.window?.let {
      it.setLayout(getWidth(), getHeight())
      it.setGravity(gravity())
      it.setBackgroundDrawable(ColorDrawable(0))
    }
  }

  @LayoutRes
  open fun getLayoutRes(): Int {
    return 0
  }

  open fun getLayout(inflater: LayoutInflater, parent : ViewGroup?) : View {
    return inflater.inflate(
      getLayoutRes(),
      parent,
      false
    )
  }

  fun <T : View> findViewById(id : Int) : T {
    return view?.findViewById(id)!!

  }

  @Style
  open fun getStyle(): Int {
    return Style.STYLE_CENTER
  }

  private fun gravity(): Int {
    return if (getStyle() == Style.STYLE_CENTER) {
      Gravity.CENTER
    } else {
      Gravity.BOTTOM
    }
  }

  open fun getWidth(): Int {
    return if (getStyle() == Style.STYLE_BOTTOM) {
      MATCH_PARENT
    } else {
      MATCH_PARENT
    }
  }

  open fun getHeight(): Int {
    return HEIGHT
  }

  open fun getTitle() : String? {
    return null
  }

  private fun lp(): LayoutParams {
    return LayoutParams(
      MATCH_PARENT,
      MATCH_PARENT
    )
  }

  private fun bg(): Int {
    return if (getStyle() == Style.STYLE_BOTTOM) {
      R.drawable.dialog_bg_bottom
    } else {
      R.drawable.dialog_bg_center
    }
  }

  annotation class Style {
    companion object {
      const val STYLE_CENTER = 1
      const val STYLE_BOTTOM = 2
    }
  }
}