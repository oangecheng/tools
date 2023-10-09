package com.orange.zax.dstclient.pages

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.orange.zax.dstclient.AdminAccount
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.data.User
import com.orange.zax.dstclient.utils.DstAlert
import com.orange.zax.dstclient.utils.ToastUtil
import com.orange.zax.dstclient.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2023/9/6
 * Author: chengzhi@kuaishou.com
 */
class UserActivity :DstActivity() {

  override fun getLayoutRes(): Int {
    return R.layout.dst_user_layout
  }

  override fun onBind() {
    val userIdView = findViewById<EditText>(R.id.userid)
    findViewById<View>(R.id.query).onClickFilter {
      queryUserInfo(Utils.emptyIfNull(userIdView.text.toString().trim()))
    }

    val giveWhite = findViewById<View>(R.id.give_role)
    if (AdminAccount.isMaster()) {
      giveWhite.visibility = View.VISIBLE
      giveWhite.setOnClickListener {
        giveUserWhite(userIdView.text.toString().trim())
      }
    }
  }

  private fun queryUserInfo(userId: String) {
    Utils.adminCheck { name, pwd ->
      DstSkinApiService.get()
        .queryUserInfo(name, pwd, userId)
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ user ->
          showUserInfoDialog(user)
        }, {
          ErrorConsumer(this).accept(it)
        })
        .also {
          autoDispose(it)
        }
    }
  }

  private fun showUserInfoDialog(user: User) {
    val info = findViewById<EditText>(R.id.skin_id).text.toString().trim()
    if (!TextUtils.isEmpty(info)) {
      if (user.skins.find { TextUtils.equals(it.skinId, info) ||
          TextUtils.equals(it.skinName, info)} != null
      ) {
        "已拥有该皮肤!"
      } else {
        "未获得该皮肤!"
      }.let {
        DstAlert.alert(this, it)
      }
      return
    }

    val items = user.skins
      .map { "${it.skinId} ${it.skinName}" }
      .ifEmpty { listOf("该用户暂未获得任何皮肤") }
      .toTypedArray()
    AlertDialog.Builder(this)
      .setTitle("已解锁的皮肤列表")
      .setItems(items) { _, _ -> }
      .setNegativeButton("我知道了") { _, _ -> }
      .show()
  }


  private fun giveUserWhite(userId: String) {
    Utils.adminCheck { name, pwd ->
      val msg = "确定给予用户 $userId 白名单权限？"
      DstAlert.alert(this, msg) {
        DstSkinApiService.get()
          .giveUserRole(name, pwd, userId, 1)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
               ToastUtil.showShort("给予权限成功")
          }, {
            ErrorConsumer(this).accept(it)
          })
          .also {
            autoDispose(it)
          }
      }
    }
  }
}
