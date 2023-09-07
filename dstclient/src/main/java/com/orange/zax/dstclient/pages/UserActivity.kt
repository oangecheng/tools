package com.orange.zax.dstclient.pages

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.api.TestConfig
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2023/9/6
 * Author: chengzhi@kuaishou.com
 */
class UserActivity :DstActivity() {

  private lateinit var resultView: TextView

  override fun getLayoutRes(): Int {
    return R.layout.dst_user_layout
  }

  override fun onBizInit() {
    val userIdView = findViewById<EditText>(R.id.userid)
    resultView = findViewById(R.id.result)
    findViewById<View>(R.id.query).onClickFilter {
      queryUserInfo(Utils.emptyIfNull(userIdView.text?.toString()))
    }
  }

  private fun queryUserInfo(userId: String) {
    Utils.adminCheck { name, pwd ->
      DstSkinApiService.get()
        .queryUserInfo(name, pwd, userId)
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ user ->
          Log.d(TestConfig.TAG, "user=$user")
          var result = ""
          user.skins.forEach {
            result += it.skinId + "\n"
          }
          resultView.text = result

        }, {
          ErrorConsumer(this).accept(it)
        })
        .also {
          autoDispose(it)
        }
    }
  }
}