package com.orange.zax.dstclient.pages

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.orange.zax.dstclient.AdminAccount
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.data.Skin
import com.orange.zax.dstclient.utils.ToastUtil
import com.orange.zax.dstclient.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2023/9/6
 * Author: chengzhi@kuaishou.com
 */
class ManageSkinActivity :Activity() {

  private lateinit var skinIdView : EditText
  private lateinit var skinNameView : EditText
  private lateinit var skinPrefabView : EditText
  private lateinit var skinTypeView : EditText


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dst_skin_manage_layout)
    skinIdView = findViewById(R.id.skin_id)
    skinNameView = findViewById(R.id.skin_name)
    skinPrefabView = findViewById(R.id.skin_prefab)
    skinTypeView = findViewById(R.id.skin_type)


    findViewById<View>(R.id.skin_register).setOnClickListener {
      skinInfo().let {
        AlertDialog.Builder(this)
          .setTitle("确定上架皮肤?")
          .setNegativeButton("取消") { _, _ -> ToastUtil.showShort("已取消") }
          .setPositiveButton("确定") { _, _ -> registerSkin(it) }
          .show()
      }
    }

    findViewById<View>(R.id.skin_update).setOnClickListener {
      skinInfo().let {
        AlertDialog.Builder(this)
          .setTitle("确定更新皮肤数据?")
          .setNegativeButton("取消") { _, _ -> ToastUtil.showShort("已取消") }
          .setPositiveButton("确定") { _, _ -> updateSkin(it) }
          .show()
      }
    }
  }

  private fun registerSkin(skin: Skin) {
    val adminName = AdminAccount.adminName?:return
    val adminPwd = AdminAccount.adminPwd ?: return
    val d = DstSkinApiService.create()
      .registerSkin(adminName, adminPwd, skin.skinId, skin.skinName, skin.skinPrefab, skin.skinType)
      .map(ResponseFunction())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        ToastUtil.showShort("皮肤上架成功!")
      }, {
        ToastUtil.showShort(it.message ?: "上架失败!")
      })
  }

  private fun updateSkin(skin: Skin) {
    val adminName = AdminAccount.adminName?:return
    val adminPwd = AdminAccount.adminPwd ?: return
    val d = DstSkinApiService.create()
      .updateSkin(adminName, adminPwd, skin.skinId, skin.skinName, skin.skinPrefab, skin.skinType)
      .map(ResponseFunction())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        ToastUtil.showShort("皮肤信息更新成功!")
      }, {
        ToastUtil.showShort(it.message ?: "更新失败!")
      })
  }

  private fun skinInfo(): Skin {
    val id = Utils.emptyIfNull(skinIdView.text.toString())
    val name = Utils.emptyIfNull(skinNameView.text.toString())
    val prefab = Utils.emptyIfNull(skinPrefabView.text.toString())
    val type = Utils.safeParseInt(skinTypeView.text?.toString())
    return Skin(id, name, prefab, type)
  }
}