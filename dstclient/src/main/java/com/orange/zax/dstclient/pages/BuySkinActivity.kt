package com.orange.zax.dstclient.pages


import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.SkinAdapter
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.utils.DstAlert
import com.orange.zax.dstclient.utils.ToastUtil
import com.orange.zax.dstclient.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
class BuySkinActivity : DstActivity() {

  private val adapter = SkinAdapter()

  override fun getLayoutRes(): Int {
    return R.layout.dst_skin_buy_layout
  }

  override fun onBind() {
    initView()
    loadSkinPage()
  }

  private fun initView() {
    val recyclerView = findViewById<RecyclerView>(R.id.skin_list)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context).apply {
      orientation = RecyclerView.VERTICAL
    }
    bindBuyBtn()
  }

  private fun bindBuyBtn() {
    val unlockBtn = findViewById<View>(R.id.unlock)
    val userEditText = findViewById<EditText>(R.id.userid)
    val priceView = findViewById<EditText>(R.id.price)
    val extraView = findViewById<EditText>(R.id.extra)

    unlockBtn.onClickFilter {
      val userId = userEditText.text?.toString()?.trim() ?: ""
      if (userId.length == 11) {
        val unlockSkins = adapter.getList()
          .filter { it.isSelected }

        if (unlockSkins.isEmpty()) {
          DstAlert.alert(this, "未选择皮肤!")
          return@onClickFilter
        }

        var priceExpect = 0
        unlockSkins.forEach {
          priceExpect+=it.skinPrice
        }

        val price = Utils.safeParseInt(priceView.text.toString())
        val extra = extraView.text.toString()
        if (price != priceExpect) {
          if (TextUtils.isEmpty(extra)) {
            DstAlert.alert(this,"价格不符合预期，请填写备注!")
            return@onClickFilter
          }
        }

        DstAlert.alert(this, "确定解锁皮肤？") {
          buySkin(userId, unlockSkins.map { it.skinId }, price, extra)
        }
      }
    }
  }


  private fun loadSkinPage() {
    Utils.adminCheck { name, pwd ->
      DstSkinApiService.get()
        .querySkinList(name, pwd)
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          it.skins?.let { list ->
            adapter.addAll(list)
          }
        }, {
          ErrorConsumer(this).accept(it)
        })
        .also {
          autoDispose(it)
        }
    }
  }


  private fun buySkin(userId: String, skinIds: List<String>, price : Int, extra : String?) {
    Utils.adminCheck { name, pwd ->
      val ids = TextUtils.join(",", skinIds)
      DstSkinApiService.get()
        .unlockSkin(name, pwd, userId, ids, price, extra)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          ToastUtil.showShort("解锁成功!")
        }, {
          ErrorConsumer(this).accept(it)
        })
        .also {
          autoDispose(it)
        }
    }
  }
}