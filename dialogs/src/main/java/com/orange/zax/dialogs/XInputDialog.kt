package com.orange.zax.dialogs

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.ustc.zax.base.utils.ViewUtil
import com.ustc.zax.dialogs.R

/**
 * Time: 2024/6/11
 * Author: chengzhi@kuaishou.com
 */
class XInputDialog : XDialog() {

  private var hint: String? = null
  private var source: String? = null
  private var onSure: ((content: String) -> Unit)? = null

  companion object {

    fun newInstance(
      source: String? = null,
      hint: String? = null,
      onSure: ((content: String) -> Unit)?
    ): XInputDialog {
      return XInputDialog().apply {
        this.source = source
        this.hint = hint
        this.onSure = onSure
      }
    }
  }

  override fun getLayoutRes(): Int {
    return R.layout.dialog_input_layout
  }

  override fun getTitle(): String {
    return "输入内容"
  }

  override fun getWidth(): Int {
    return ViewUtil.dp2px(300)
  }

  override fun getHeight(): Int {
    return ViewUtil.dp2px(300)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val et = view.findViewById<EditText>(R.id.input_et)
    hint?.let { et.hint = it }
    view.findViewById<View>(R.id.input_sure).setOnClickListener {
      onSure?.invoke(et.text.toString())
    }
  }
}