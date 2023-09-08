package com.orange.zax.dstclient.pages

import android.view.View
import android.widget.EditText
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.data.Skin
import com.orange.zax.dstclient.utils.DstAlert
import com.orange.zax.dstclient.utils.ToastUtil
import com.orange.zax.dstclient.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2023/9/6
 * Author: chengzhi@kuaishou.com
 */
class ManageSkinActivity :DstActivity(){

  private lateinit var skinIdView : EditText
  private lateinit var skinNameView : EditText
  private lateinit var skinPrefabView : EditText
  private lateinit var skinTypeView : EditText
  private lateinit var skinPriceView : EditText

  override fun getLayoutRes(): Int {
    return R.layout.dst_skin_manage_layout
  }

  override fun onBind() {
    skinIdView = findViewById(R.id.skin_id)
    skinNameView = findViewById(R.id.skin_name)
    skinPrefabView = findViewById(R.id.skin_prefab)
    skinTypeView = findViewById(R.id.skin_type)
    skinPriceView = findViewById(R.id.skin_price)

    findViewById<View>(R.id.skin_register)
      .onClickFilter {
        skinInfo().let {
          val msg = skinMsg(it)
          DstAlert.alert(this, msg) {
            registerSkin(it)
          }
        }
      }

    findViewById<View>(R.id.skin_update)
      .onClickFilter {
        skinInfo().let {
          val msg = skinMsg(it)
          DstAlert.alert(this, msg) {
            updateSkin(it)
          }
        }
      }
  }

  private fun registerSkin(skin: Skin) {
    Utils.adminCheck { name, pwd ->
      DstSkinApiService.get()
        .registerSkin(name, pwd, skin.skinId, skin.skinName, skin.skinPrefab, skin.skinType, skin.skinPrice)
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          DstAlert.alert(this, "皮肤上架成功！")
        }, {
          ErrorConsumer(this).accept(it)
        })
        .also {
          autoDispose(it)
        }
    }
  }

  private fun updateSkin(skin: Skin) {
    Utils.adminCheck { name, pwd ->
      DstSkinApiService.get()
        .updateSkin(name, pwd, skin.skinId, skin.skinName, skin.skinPrefab, skin.skinType, skin.skinPrice)
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          ToastUtil.showShort("皮肤信息更新成功!")
        }, {
          ToastUtil.showShort(it.message ?: "更新失败!")
        })
        .also {
          autoDispose(it)
        }
    }
  }

  private fun skinInfo(): Skin {
    val id = Utils.emptyIfNull(skinIdView.text.toString())
    val name = Utils.emptyIfNull(skinNameView.text.toString())
    val prefab = Utils.emptyIfNull(skinPrefabView.text.toString())
    val type = Utils.safeParseInt(skinTypeView.text?.toString())
    val price = Utils.safeParseInt(skinPriceView.text?.toString())
    return Skin(id, name, prefab, type, price)
  }

  private fun skinMsg(it: Skin): String {
    return "确认皮肤信息: \n id=${it.skinId} \n 名称=${it.skinName} \n " +
      "物品=${it.skinPrefab} \n 类型=${it.skinType} \n 价格=${it.skinPrice} "
  }
}