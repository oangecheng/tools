package com.ustc.orange.tools.pages

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.ustc.orange.tools.R
import com.ustc.orange.tools.pages.vc.XViewCTActivity
import com.ustc.orange.tools.test.XTestModel

/**
 * Time: 2024/11/20
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 */
class XJump(private val activity : Activity) : XTestModel {

  companion object {
    private val LIST = listOf("nothing", "vc test")
  }

  override fun onStart(context: Context) {
    val spinner = activity.findViewById<Spinner>(R.id.jump)
    val adapter = ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item)
    adapter.addAll(LIST)
    spinner.adapter = adapter
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 1) {
          val intent = Intent(context, XViewCTActivity::class.java)
          context.startActivity(intent)
        }
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {
      }
    }
  }

  override fun onStop() {

  }
}