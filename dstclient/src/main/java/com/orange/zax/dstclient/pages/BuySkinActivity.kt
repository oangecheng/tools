package com.orange.zax.dstclient.pages


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
import com.orange.zax.dstclient.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
class BuySkinActivity : DstActivity() {

  companion object {
    const val TYPE_CUSTOM = 2
    const val TYPE_SPONSOR = 3
    const val TYPE_KEY = "SKIN_TYPE"
  }

  private val adapter = SkinAdapter()
  private val skinList = ArrayList<Skin>()
  private var progressDialog : ProgressDialog? = null

  private var userEditText: EditText? = null
  private var priceEditText: EditText? = null
  private var extraEditText: EditText? = null
  private var unlockBtn : View? = null

  override fun getLayoutRes(): Int {
    return R.layout.dst_skin_buy_layout
  }

  override fun onBind(data: Bundle?) {
    val skinType = data?.getInt(TYPE_KEY, TYPE_SPONSOR) ?: TYPE_SPONSOR
    loadSkinPage(skinType)
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
    selectedAll.setOnClickListener { v ->
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
  }

  private fun initSkinFilter(skins : List<Skin>) {
    val selectAllText = "全部"
    val query = findViewById<Button>(R.id.query)
    val querySpinner = findViewById<Spinner>(R.id.query_info)
    val spinnerAdapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item)
    val data = skins.map { it.skinPrefab }.toSet().toList()
    spinnerAdapter.add(selectAllText)
    spinnerAdapter.addAll(data)
    querySpinner.adapter = spinnerAdapter

    query.setOnClickListener { _ ->
      val info = querySpinner.selectedItem
      adapter.clear()
      if (info == null || TextUtils.equals(info as String, selectAllText)) {
        adapter.addAll(skins)
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
    unlockBtn = findViewById(R.id.unlock)
    userEditText = findViewById(R.id.userid)
    priceEditText = findViewById(R.id.price)
    extraEditText = findViewById(R.id.extra)

    unlockBtn?.onClickFilter {
      val userId = userEditText?.text?.toString()?.trim() ?: ""
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

        val price = Utils.safeParseInt(priceEditText?.text.toString())
        val extra = extraEditText?.text.toString()
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


  private fun loadSkinPage(skinType : Int) {
    Utils.adminCheck { _, _ ->
      DstSkinApiService.get()
        .querySkinList()
        .map(ResponseFunction())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ res ->
          res.skins
            ?.filter {
              it.skinType == skinType
            }?.let { list ->
              skinList.addAll(list)
              adapter.setList(list)
              initView()
              initSkinFilter(skinList)
            }
        }, {
          ErrorConsumer(this).accept(it)
        })
        .also {
          autoDispose(it)
        }
    }
  }


  @SuppressLint("NotifyDataSetChanged")
  private fun buySkin(userId: String, skinIds: List<String>, price : Int, extra : String?) {
    Utils.adminCheck { _, _ ->

      unlockBtn?.isClickable = false
      progressDialog?.dismiss()
      progressDialog = ProgressDialog.show(this, "解锁皮肤中", "请稍后...")
      progressDialog?.show()

      val ids = TextUtils.join(",", skinIds)
      DstSkinApiService.get()
        .unlockSkin(userId, ids, price, extra)
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally {
          progressDialog?.dismiss()
          resetEditView()
        }
        .subscribe({
          adapter.getList().forEach { it.isSelected = false }
          adapter.notifyDataSetChanged()
          DstAlert.alert(this, "解锁成功!")
        }, {
          ErrorConsumer(this).accept(it)
        })
        .also {
          autoDispose(it)
        }
    }
  }

  private fun resetEditView() {
    userEditText?.text = null
    priceEditText?.text = null
    extraEditText?.text = null
    unlockBtn?.isClickable = true
  }
}