package com.ustc.orange.tools.test.other

import android.content.Context
import com.ustc.orange.tools.test.XTestModel
import com.ustc.zax.tool.XJsonCompare

/**
 * Time: 2024/7/18
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
class JsonTestModel : XTestModel {

  override fun onStart(context: Context) {
    val utils = XJsonCompare()
    utils.test()
  }

  override fun onStop() {

  }
}