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
import android.widget.TextView
import androidx.collection.ArraySet
import com.bumptech.glide.Glide
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ImageUploader
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.api.XGson
import com.orange.zax.dstclient.biz.homepage.data.ItemCache
import com.orange.zax.dstclient.biz.homepage.data.ItemType
import com.orange.zax.dstclient.biz.homepage.data.Prefab
import com.orange.zax.dstclient.utils.ToastUtil
import com.ustc.zax.base.fragment.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


/**
 * Time: 2024/12/3
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class PageItem : BaseFragment() {

  companion object {

    private const val TYPE_KEY = "type"
    private const val EMPTY = ""

    fun instance(@ItemType type: Int) : BaseFragment {
      val page = PageItem()
      val args = Bundle()
      args.putInt(TYPE_KEY, type)
      page.arguments = args
      return page
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

    val type = arguments?.getInt(TYPE_KEY) ?: 0

    view.findViewById<TextView>(R.id.title).text = if (type == ItemType.RECIPE) {
      "配方物品"
    } else {
      "图片资源"
    }


    vAdd.setOnClickListener {
      val id = vId.text.toString().trim()
      val name = vName.text.toString().trim()
      val url = vUrl.text.toString().trim()
      if (id.isNotEmpty() && name.isNotEmpty() && url.isNotEmpty()) {
        addPrefab(type, Prefab(id, name, url)) {
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
        PageApiService.get().updateItem(id, data, type)
          .map(ResponseFunction())
          .subscribe({
            vId.setText(EMPTY)
            vName.setText(EMPTY)
            vUrl.setText(EMPTY)
            vUpdate.text = "查询物品"
            ItemCache.cache(type, Prefab(id, name, url))
          }, {

          }).also {
            autoDispose(it)
          }
      } else if (id.isNotEmpty()) {
        ItemCache.item(type, id)?.let {
          vId.setText(it.id)
          vName.setText(it.name)
          vUrl.setText(it.url)
          vUpdate.text = "更新物品"
        }
      }
    }

    vDel.setOnClickListener {
      val id = vId.text.toString().trim()
      if (id.isNotEmpty()) {
        PageApiService.get().deleteItem(id)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            ItemCache.remove(type, id)
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


  private fun addPrefab(type: Int, prefab: Prefab, invoke: () -> Unit): Disposable {
    val data = XGson.GSON.toJson(prefab)
    return PageApiService.get()
      .addItem(prefab.id, data, type)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        ToastUtil.showLong("添加成功")
        ItemCache.cache(type, prefab)
        invoke()
      }, {
        ErrorConsumer().accept(it)
      })
  }

}