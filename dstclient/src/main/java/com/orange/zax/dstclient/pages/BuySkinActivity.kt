package com.orange.zax.dstclient.pages

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.zax.dstclient.AdminAccount
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.SkinAdapter
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.utils.ToastUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
class BuySkinActivity : Activity() {


  private var refreshDisposable: Disposable? = null
  private var buySkinDisposable: Disposable? = null
  private val adapter = SkinAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dst_skin_buy_layout)
    initView()
    loadSkinPage()
  }

  override fun onDestroy() {
    super.onDestroy()
    refreshDisposable?.dispose()
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
    unlockBtn.setOnClickListener {
      val userId = userEditText.text?.toString()?.trim() ?: ""
      if (userId.length == 11) {
        val unlockSkins = adapter.getList()
          .filter { it.isSelected }
          .map { it.skinId }

        if (unlockSkins.isEmpty()) {
          ToastUtil.showShort("未选择任何皮肤")
          return@setOnClickListener
        }

        AlertDialog.Builder(this)
          .setTitle("确定解锁皮肤")
          .setNegativeButton("取消") { _, _ -> ToastUtil.showShort("已取消") }
          .setPositiveButton("确定") { _, _ -> buySkin(userId, unlockSkins) }
          .show()
      }
    }
  }


  private fun loadSkinPage() {
    refreshDisposable?.dispose()
    refreshDisposable = DstSkinApiService.create().querySkinList(
      AdminAccount.adminName ?: "",
      AdminAccount.adminPwd ?: ""
    )
      .map(ResponseFunction())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        it.skins?.let { list ->
          adapter.addAll(list)
        }
      }, {
        ToastUtil.showShort(it.message?:"网络异常")
      })
  }


  private fun buySkin(userId: String, skinIds: List<String>) {
    val adminName = AdminAccount.adminName ?: return
    val adminPwd = AdminAccount.adminPwd ?: return
    if (skinIds.isNotEmpty()) {
      val ids = TextUtils.join(",", skinIds)
      buySkinDisposable?.dispose()
      buySkinDisposable = DstSkinApiService.create()
        .unlockSkin(adminName, adminPwd, userId, ids)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          ToastUtil.showShort("解锁成功!")
        }, {
          ToastUtil.showShort(it.message ?: "网络异常")
        })
    }
  }
}