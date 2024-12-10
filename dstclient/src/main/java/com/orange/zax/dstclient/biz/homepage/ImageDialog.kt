package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.orange.zax.dialogs.XDialog
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.biz.homepage.data.ItemCache
import com.orange.zax.dstclient.biz.homepage.data.ItemType
import com.orange.zax.dstclient.biz.homepage.data.Prefab
import com.ustc.zax.base.recycler.BaseRecyclerAdapter
import java.text.Collator
import java.util.ArrayList
import java.util.Locale

/**
 * Time: 2024/5/30
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class ImageDialog : XDialog() {

  companion object {
    fun instance(listener: Listener) : DialogFragment {
      val dialog = ImageDialog()
      dialog.listener = listener
      return dialog
    }
  }

  private lateinit var listView: RecyclerView
  private var listener : Listener? = null


  private val adapter = ImageAdapter(object : Listener {
    override fun onSelected(prefab: Prefab) {
      listener?.onSelected(prefab)
      dismissAllowingStateLoss()
    }
  })

  override fun getLayoutRes(): Int {
    return R.layout.dialog_list_layout
  }



  override fun getTitle(): String {
    return "选择图片"
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    listView = view.findViewById(R.id.list)
    initListView(listView)
  }


  private fun initListView(view: RecyclerView) {
    listView.layoutManager = LinearLayoutManager(view.context)
    listView.adapter = adapter
    val allItems = ItemCache.items(ItemType.IMAGE)
    // 排序，数量不为0的放前面
    val chinaCollator = Collator.getInstance(Locale.CHINESE)
    val list = allItems.sortedWith(
      compareByDescending<Prefab> { it.id }.thenBy(chinaCollator) { it.name }
    )
    adapter.addAll(list)
  }

  interface Listener {
    fun onSelected(prefab: Prefab)
  }

  override fun getStyle(): Int {
    return Style.STYLE_BOTTOM
  }
}


private class ImageHolder(private val view : View, private val listener: ImageDialog.Listener) : ViewHolder(view) {

  private val preview = view.findViewById<ImageView>(R.id.preview)
  private val url = view.findViewById<TextView>(R.id.url)
  private val context = view.context

  fun bind(prefab: Prefab) {
    Glide.with(context)
      .load(prefab.url)
      .into(preview)
    url.text = prefab.name

    view.setOnClickListener {
     listener.onSelected(prefab)
    }
  }

  fun unbind() {
    Glide.with(context).clear(preview)
  }
}


private class ImageAdapter(private val listener: ImageDialog.Listener) : BaseRecyclerAdapter<Prefab, ImageHolder>(){

  override fun onCreateViewHolder(parent: ViewGroup, type: Int): ImageHolder {
    return ImageHolder(
      LayoutInflater
        .from(parent.context)
        .inflate(
          R.layout.dst_homepage_image_item,
          parent,
          false
        ),
      listener
    )
  }

  override fun onBindViewHolder(holder: ImageHolder, position: Int) {
    getItem(position)?.let {
      holder.bind(it)
    }
  }

  override fun onViewRecycled(holder: ImageHolder) {
    super.onViewRecycled(holder)
    holder.unbind()
  }
}