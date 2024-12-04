package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.collection.ArraySet
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)


    val vId = view.findViewById<EditText>(R.id.input_image_id)
    val vName = view.findViewById<EditText>(R.id.input_image_name)
    val vUrl = view.findViewById<EditText>(R.id.input_image_url)
    val vBtn =  view.findViewById<View>(R.id.btn_sure)

    vBtn.setOnClickListener {
      val id = vId.text.toString().trim()
      val name = vName.text.toString().trim()
      val url = vUrl.text.toString().trim()
      if (id.isNotEmpty() && name.isNotEmpty() && url.isNotEmpty()) {
        addPrefab(Prefab(id, name, url))
      }
    }
  }


  private fun addPrefab(prefab: Prefab) : Disposable {
    val data = XGson.GSON.toJson(prefab)
    return PageApiService.get()
      .addItem(prefab.id, data, ItemType.RECIPE)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        ToastUtil.showLong("添加成功")
        ITEMS.add(prefab)
      }, {
        ErrorConsumer().accept(it)
      })
  }

}