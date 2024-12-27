package com.orange.zax.dstclient.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import com.ustc.zax.base.utils.ViewUtil

/**
 * Time: 2024/12/26
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class XInputTextView(
  context: Context, attr: AttributeSet?, attrStyle: Int
) : AppCompatTextView(context, attr, attrStyle), InputDialog.Listener {

  constructor(context: Context) : this(context, null, 0)
  constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

  override fun onSure(content: String) {
    text = content
  }

  init {
    setOnClickListener {
      onEdit()
    }
  }

  private fun onEdit() {
    val activity = ViewUtil.activity(this) as? FragmentActivity ?: return
    val dialog = InputDialog()
    dialog.initText = text.toString()
    dialog.listener = this
    dialog.show(
      activity.supportFragmentManager,
      "XInputTextViewDialog"
    )
  }
}