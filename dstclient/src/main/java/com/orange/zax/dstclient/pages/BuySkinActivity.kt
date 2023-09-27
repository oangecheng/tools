package com.orange.zax.dstclient.pages


import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
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
import com.orange.zax.dstclient.data.Skin
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
  private val skinList = ArrayList<Skin>()

  override fun getLayoutRes(): Int {
    return R.layout.dst_skin_buy_layout
  }

  override fun onBind() {
    initView()
    loadSkinPage()
  }

  @SuppressLint("NotifyDataSetChanged")
  private fun initView() {
    val recyclerView = findViewById<RecyclerView>(R.id.skin_list)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context).apply {
      orientation = RecyclerView.VERTICAL
    }
    bindBuyBtn()

    val selectedAll = findViewById<Button>(R.id.select_all)
    selectedAll.setOnClickListener {v->
      v.isSelected = !v.isSelected

      adapter.getList().forEach {
        it.isSelected = v.isSelected
      }
      adapter.notifyDataSetChanged()

      selectedAll.text = if (v.isSelected) {
        "取消全选"
      } else {
        "选择全部"
      }
    }

    val query = findViewById<Button>(R.id.query)
    val queryInfo = findViewById<EditText>(R.id.query_info)
    query.setOnClickListener { _ ->
      val info = queryInfo.text.toString().trim()
      adapter.clear()
      if (TextUtils.isEmpty(info)) {
        adapter.addAll(skinList)
      } else {
        skinList.filter {
          TextUtils.equals(it.skinPrefab, info)
        }.let {
          adapter.addAll(it)
        }
      }
    }
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

        val msg = TextUtils.join(",", unlockSkins.map { it.skinName })
        DstAlert.alert(this, "确定解锁皮肤？", msg) {
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
        .subscribe({ res ->
          res.skins?.let { list ->
            skinList.addAll(list)
            adapter.setList(list)
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