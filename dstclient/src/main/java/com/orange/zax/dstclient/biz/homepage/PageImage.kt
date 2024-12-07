package com.orange.zax.dstclient.biz.homepage

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.collection.ArraySet
import com.bumptech.glide.Glide
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ImageUploader
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.api.XGson
import com.orange.zax.dstclient.biz.homepage.data.ItemType
import com.orange.zax.dstclient.biz.homepage.data.Prefab
import com.orange.zax.dstclient.utils.ToastUtil
import com.ustc.zax.base.fragment.BaseFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


/**
 * Time: 2024/12/3
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class PageImage : BaseFragment() {

  companion object {

    private val ITEMS = ArraySet<Prefab>()
    private const val EMPTY = ""

    fun instance() : BaseFragment {
      return PageImage()
    }

    fun items(): Observable<ArraySet<Prefab>>{
      if (ITEMS.isEmpty()) {
        return PageApiService.get()
          .typeItems(ItemType.RECIPE)
          .map(ResponseFunction())
          .map { resp ->
            resp.items.map {
              XGson.parse(it.data, Prefab::class.java) ?: Prefab("", "", "")
            }.filter {
              it.id.isNotEmpty()
            }
          }
          .observeOn(AndroidSchedulers.mainThread())
          .doOnNext {
            ITEMS.clear()
            ITEMS.addAll(it)
          }.map {
            ITEMS
          }
      }
      return Observable.just(ITEMS)
        .observeOn(AndroidSchedulers.mainThread())
    }
  }

  private var imageLoader : ImageUploader? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(
      R.layout.dst_homepage_image,
      container,
      false
    )
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode === 100 && resultCode === RESULT_OK && data != null) {
      val imageUri: Uri? = data.data
      val id = view?.findViewById<EditText>(R.id.input_image_id)?.text?.toString()?.trim()
      if (imageUri != null && id?.isNotEmpty() == true) {
        imageLoader?.onSelect(imageUri, id) { url ->
          view?.findViewById<EditText>(R.id.input_image_url)?.setText(url)
          view?.findViewById<ImageView>(R.id.image)?.let {
            Glide.with(this).load(url).into(it)
          }
        }

      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    imageLoader = ImageUploader(this)

    val vId = view.findViewById<EditText>(R.id.input_image_id)
    val vName = view.findViewById<EditText>(R.id.input_image_name)
    val vUrl = view.findViewById<EditText>(R.id.input_image_url)
    val vAdd =  view.findViewById<View>(R.id.btn_add)
    val vUpdate = view.findViewById<Button>(R.id.btn_update)
    val vDel = view.findViewById<View>(R.id.btn_delete)
    val vImg = view.findViewById<ImageView>(R.id.image)

    vAdd.setOnClickListener {
      val id = vId.text.toString().trim()
      val name = vName.text.toString().trim()
      val url = vUrl.text.toString().trim()
      if (id.isNotEmpty() && name.isNotEmpty() && url.isNotEmpty()) {
        addPrefab(Prefab(id, name, url)) {
          vId.setText(EMPTY)
          vName.setText(EMPTY)
          vUrl.setText(EMPTY)
        }
      }
    }

    view.findViewById<View>(R.id.input_image_url_clear).setOnClickListener {
      vUrl.setText(EMPTY)
    }

    view.findViewById<View>(R.id.input_image_url_select).setOnClickListener {
      if (vId.text.isNotEmpty()) {
        imageLoader?.open()
      } else {
        ToastUtil.showShort("请输入文件id")
      }
    }

    vUpdate.setOnClickListener {
      val id = vId.text.toString().trim()
      val name = vName.text.toString().trim()
      val url = vUrl.text.toString().trim()

      if (id.isNotEmpty() && name.isNotEmpty() && url.isNotEmpty()) {
        val data = XGson.GSON.toJson(Prefab(id, name, url))
        PageApiService.get().updateItem(id, data, ItemType.RECIPE)
          .map(ResponseFunction())
          .subscribe({
            vId.setText(EMPTY)
            vName.setText(EMPTY)
            vUrl.setText(EMPTY)
            vUpdate.text = "查询物品"
            ITEMS.removeAll { i -> i.id == id }
            ITEMS.add(Prefab(id, name, url))
          }, {
          }).also {
            autoDispose(it)
          }
      } else if (id.isNotEmpty()) {
        PageApiService.get().queryItem(id)
          .map(ResponseFunction())
          .filter { it.type == ItemType.RECIPE }
          .map { XGson.parse(it.data, Prefab::class.java)?:Prefab("", "", "") }
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            vId.setText(it.id)
            vName.setText(it.name)
            vUrl.setText(it.url)
            vUpdate.text = "更新物品"
          }, {
          }).also {
            autoDispose(it)
          }
      }
    }

    vDel.setOnClickListener {
      val id = vId.text.toString().trim()
      if (id.isNotEmpty()) {
        PageApiService.get().deleteItem(id)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            ToastUtil.showShort("删除成功")
          }, {
            ErrorConsumer().accept(it)
          }).also {
            autoDispose(it)
          }
      }
    }

    vImg.setOnClickListener {
      val url = vUrl.text.toString().trim()
      if (url.isNotEmpty()) {
        Glide.with(this)
          .load(url)
          .into(vImg)
      }
    }
  }


  private fun addPrefab(prefab: Prefab, invoke: () -> Unit): Disposable {
    val data = XGson.GSON.toJson(prefab)
    return PageApiService.get()
      .addItem(prefab.id, data, ItemType.RECIPE)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        ToastUtil.showLong("添加成功")
        ITEMS.add(prefab)
        invoke()
      }, {
        ErrorConsumer().accept(it)
      })
  }

}