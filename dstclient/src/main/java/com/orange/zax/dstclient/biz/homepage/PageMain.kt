package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.orange.zax.dstclient.BackPressInterceptor
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.biz.homepage.data.ItemCache
import com.ustc.zax.base.fragment.BaseFragment
import com.ustc.zax.base.recycler.BaseRecyclerAdapter
import java.text.Collator
import java.util.Locale

/**
 * Time: 2024/12/11
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class PageMain : BaseFragment(), BackPressInterceptor {

  companion object {
    fun instance(): BaseFragment {
      return PageMain()
    }
  }

  private lateinit var listView: RecyclerView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(
      R.layout.dst_homepage_main,
      container,
      false
    )
  }

  override fun intercept(): Boolean {
    return if (childFragmentManager.backStackEntryCount > 0) {
      childFragmentManager.popBackStack()
      true
    } else {
      false
    }
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    listView = view.findViewById(R.id.list)
    initListView(listView)
    view.findViewById<View>(R.id.add).onClickFilter {
      change(PageAdd.instance())
    }
  }

  private fun initListView(view: RecyclerView) {
    view.layoutManager = LinearLayoutManager(view.context)
    val adapter = Adapter()
    view.adapter = adapter
    val chinaCollator = Collator.getInstance(Locale.CHINESE)
    ItemCache.observeNormal(this, Observer { l ->
      val list = l.sortedWith(Comparator { o1, o2 -> chinaCollator.compare(o1.name, o2.name) })
      adapter.setList(list)
    })
  }

  private fun update(data: ItemData) {
    change(PageUpdate.instance(data))
  }

  private fun change(page: BaseFragment) {
    val id = R.id.container
    childFragmentManager
      .beginTransaction()
      .replace(id, page)
      .addToBackStack("container")
      .commitAllowingStateLoss()
  }


  private inner class Adapter : BaseRecyclerAdapter<ItemData, Holder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
      return Holder(
        LayoutInflater
          .from(parent.context)
          .inflate(
            R.layout.dst_homepage_main_item,
            parent,
            false
          )
      )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
      getItem(position)?.let {
        holder.bind(it)
      }
    }
  }


  private inner class Holder(private val view: View) : ViewHolder(view) {
    private val image = view.findViewById<ImageView>(R.id.image)
    private val desc = view.findViewById<TextView>(R.id.desc)
    private val update = view.findViewById<TextView>(R.id.update)

    fun bind(item: ItemData) {
      Glide.with(view.context)
        .load(item.image)
        .into(image)
      desc.text = item.name
      update.setOnClickListener {
        update(item)
      }
    }

  }
}

