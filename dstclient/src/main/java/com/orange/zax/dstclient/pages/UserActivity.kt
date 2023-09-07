package com.orange.zax.dstclient.pages

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.orange.zax.dstclient.AdminAccount
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.utils.ToastUtil
import com.orange.zax.dstclient.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2023/9/6
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class UserActivity :Activity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dst_user_layout)

    val userIdView = findViewById<EditText>(R.id.userid)
    val resultView = findViewById<TextView>(R.id.result)
    findViewById<View>(R.id.query).setOnClickListener {
      val d = DstSkinApiService.create().queryUserInfo(
        Utils.emptyIfNull(AdminAccount.adminName),
        Utils.emptyIfNull(AdminAccount.adminPwd),
        Utils.emptyIfNull(userIdView.text?.toString())
      )
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ user ->
          var result = ""
          user.skins.forEach {
            result += it.skinId + "\n"
          }
          resultView.text = result

        }, {
          ToastUtil.showShort(it.message ?: "未知错误")
        })
    }
  }
}