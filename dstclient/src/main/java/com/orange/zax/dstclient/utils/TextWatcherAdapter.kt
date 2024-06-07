package com.orange.zax.dstclient.utils

import android.text.Editable
import android.text.TextWatcher

/**
 * Time: 2024/5/30
 * Author: chengzhi@kuaishou.com
 */
interface TextWatcherAdapter : TextWatcher {
  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
  }

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
  }

  override fun afterTextChanged(s: Editable?) {
  }
}