package com.orange.zax.dstclient

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import com.orange.zax.dstclient.api.ResponseFunction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Time: 2023/9/1
 * Author: chengzhi@kuaishou.com
 */
class DstLoginActivity : Activity() {

  private val isTest = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dst_login_layout)

    val usernameView : EditText = findViewById(R.id.username)
    val pwdView : EditText = findViewById(R.id.password)
    val btn : View = findViewById(R.id.login)

    btn.setOnClickListener {
      val name = if(isTest) "orange" else usernameView.text.toString()
      val password = if(isTest) "940512" else  pwdView.text.toString()
      if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
        val dis = DstSkinApiService.create()
          .login(name, password)
          .map(ResponseFunction())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
              AdminAccount.adminName = name
              AdminAccount.adminPwd = password
              val intent = Intent()
              intent.setClass(this, DstHomeActivity::class.java)
              startActivity(intent)
              this.finish()
          }, {
            Log.e("orangeTest", "login error", it)
          })
      }
    }
  }

  private fun test() {
    val d = DstSkinApiService.create()
      .test()
      .subscribeOn(Schedulers.io())
      .observeOn(Schedulers.io())
      .subscribe({
        Log.d("orangeTest", "login success")
      }, {
        Log.e("orangeTest", "login error", it)
      })
  }

}