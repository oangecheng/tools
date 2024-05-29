package com.orange.zax.dstclient.pages

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.gson.Gson
import com.orange.zax.dstclient.AdminAccount
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.PageApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.api.TestConfig
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.data.ItemInfo
import com.orange.zax.dstclient.data.Recipe
import com.orange.zax.dstclient.utils.DstAlert
import com.orange.zax.dstclient.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Time: 2023/9/1
 * Author: chengzhi@kuaishou.com
 */
class DstLoginActivity : DstActivity() {

  private val isTest = TestConfig.isTest()

  override fun getLayoutRes(): Int {
    return R.layout.dst_login_layout
  }

  override fun onBind(data: Bundle?) {

    jump(DstHomepageActivity::class.java)

//    val a = PageApiService.get().getAllItems()
//      .observeOn(AndroidSchedulers.mainThread()).subscribe {
//      Log.d("OrangeTest", it.toString())
//    }
//
//    val item = ItemInfo(
//      "zxlight",
//      "永亮灯",
//      "漂亮的小灯",
//      listOf(1),
//      2,
//      "二本科技制作",
//      listOf(
//        Recipe(
//          "cutstone",
//          2
//        )
//      )
//    )
//
//    val b = PageApiService.get().addItem(
//      item.id,
//      Gson().toJson(item)
//    )
//      .delay(10, TimeUnit.SECONDS)
//      .observeOn(AndroidSchedulers.mainThread())
//      .map(ResponseFunction())
//      .subscribe( {
//        Log.d("OrangeTest", it.toString())
//      }, {
//        Log.e("OrangeTest", "error", it)
//      })

    val usernameView : EditText = findViewById(R.id.username)
    val pwdView : EditText = findViewById(R.id.password)
    val btn : View = findViewById(R.id.login)

    btn.onClickFilter {
      val name = if (isTest) "orange" else usernameView.text.toString().trim()
      val password = if (isTest) "940512" else pwdView.text.toString().trim()
      if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
        startLogin(name, password)
      } else {
        DstAlert.alert(this, "账号密码错误!")
      }
    }
  }

  private fun startLogin(username: String, password: String) {
    AdminAccount.clear()
    DstSkinApiService.get()
      .login(username, password)
      .map(ResponseFunction())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        AdminAccount.adminName = username
        AdminAccount.adminPwd = password
        Utils.jumpActivityAnFinish(this, DstHomeActivity::class.java)
      }, {
        ErrorConsumer(this).accept(it)
      })
      .also {
        autoDispose(it)
      }
  }
}