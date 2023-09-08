package com.orange.zax.dstclient.pages

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.orange.zax.dstclient.AdminAccount
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.api.TestConfig
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.utils.DstAlert
import com.orange.zax.dstclient.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2023/9/1
 * Author: chengzhi@kuaishou.com
 */
class DstLoginActivity : DstActivity() {

  private val isTest = TestConfig.isTest()

  override fun getLayoutRes(): Int {
    return R.layout.dst_login_layout
  }

  override fun onBind() {
    val usernameView : EditText = findViewById(R.id.username)
    val pwdView : EditText = findViewById(R.id.password)
    val btn : View = findViewById(R.id.login)

    btn.onClickFilter {
      val name = if (isTest) "orange" else usernameView.text.toString()
      val password = if (isTest) "940512" else pwdView.text.toString()
      if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
        startLogin(name, password)
      } else {
        DstAlert.alert(this, "账号密码错误!")
      }
    }
  }

  private fun startLogin(username: String, password: String) {
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