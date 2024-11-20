package com.ustc.orange.tools.pages.vc

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.kuaishou.live.ext.addViewController
import com.ustc.orange.tools.R
import com.ustc.zax.tool.Utils

/**
 * Time: 2024/11/20
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class XViewCTActivity : AppCompatActivity() {

  private val mainVC = XViewMain()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_vc)
    init()

    Utils.delayTask(10, Runnable {
      addViewController(R.id.container,  mainVC)
    })
  }


  private fun init() {
    val spinner = findViewById<Spinner>(R.id.select_entrance)
    val adapter = ArrayAdapter<Int>(this, R.layout.support_simple_spinner_dropdown_item)
    val list = listOf(1, 2, 3, 4)
    adapter.addAll(list)
    spinner.adapter = adapter

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val p = position + 1
        mainVC.changeViewController(p)
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {
      }
    }
  }
}

