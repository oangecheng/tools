package com.ustc.orange.tools.test.other

import android.content.Context
import android.content.Intent
import com.ustc.orange.tools.test.XTestModel
import com.ustc.zax.base.GlobalConfig
import com.ustc.zax.tool.SystemUtils.checkInstalledApps
import com.ustc.zax.tool.Utils

/**
 * Time: 2024/7/18
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
class AppTestModel : XTestModel {


  companion object {
    private const val TEST_DESKTOP = false
    private const val JSON = "{\n" +
      "  \"identifiers\" : [\n" +
      "  \"br.com.gabba.Caixa\",\n" +
      "  \"com.bradesco\",\n" +
      "  \"com.itau\",\n" +
      "  \"br.com.bb.android\",\n" +
      "  \"com.santander.app\"\n" +
      "  ]\n" +
      "}\n";
  }


  override fun onStart(context: Context) {
    checkInstalledApps(context, JSON) {
      it.apps
    }
    if (TEST_DESKTOP) {
      Utils.delayTask(10000, this, Runnable {
        backToDesktop()
      })
    }
  }

  override fun onStop() {
    Utils.cancelTask(this)
  }

  private fun backToDesktop() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addCategory(Intent.CATEGORY_HOME)
    GlobalConfig.application.startActivity(intent)
  }
}