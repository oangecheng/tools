package com.orange.zax.dstclient.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.orange.zax.dialogs.XDialog
import com.orange.zax.dstclient.R
import com.ustc.zax.base.utils.ViewUtil

/**
 * Time: 2024/6/7
 * Author: chengzhi@kuaishou.com
 */

class InputDialog : XDialog() {

  var listener : Listener? = null
  var initText : String? = null

  override fun getLayoutRes(): Int {
    return R.layout.dst_input_dialog
  }

  override fun getStyle(): Int {
    return Style.STYLE_BOTTOM
  }

  override fun getHeight(): Int {
    return ViewUtil.dp2px(600)
  }

  override fun getTitle(): String {
    return "编辑内容"
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val et = findViewById<EditText>(R.id.content_text)
    initText?.let {
      et.setText(it)
    }

    findViewById<View>(R.id.sure).setOnClickListener {
      listener?.onSure(et.text.toString())
      dismissAllowingStateLoss()
    }
    findViewById<View>(R.id.cancel).setOnClickListener {
      dismissAllowingStateLoss()
    }
  }

  interface Listener {
    fun onSure(content : String)
  }
}