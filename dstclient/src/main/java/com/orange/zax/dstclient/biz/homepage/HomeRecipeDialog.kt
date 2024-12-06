package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.collection.ArraySet
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orange.zax.dialogs.XDialog
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.biz.homepage.data.Prefab
import com.orange.zax.dstclient.utils.TextWatcherAdapter
import com.ustc.zax.base.recycler.BaseRecyclerAdapter
import java.text.Collator
import java.util.Locale

/**
 * Time: 2024/5/30
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
class HomeRecipeDialog : XDialog() {

  companion object {
    fun instance(data : List<Recipe>?, prefabs: Set<Prefab>, listener: Listener) : DialogFragment {
      val dialog = HomeRecipeDialog()
      dialog.listener = listener
      dialog.data = data
      dialog.prefabs.addAll(prefabs)
      return dialog
    }
  }

  private lateinit var recipeListView: RecyclerView
  private val adapter = RecipeAdapter()

  private var listener : Listener? = null
  private var data : List<Recipe>? = null
  private var prefabs = ArraySet<Prefab>()


  override fun getLayoutRes(): Int {
    return R.layout.dst_homepage_recipe
  }

  override fun getTitle(): String {
    return "选择配方"
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    recipeListView = view.findViewById(R.id.recipe_list)
    initListView(recipeListView)

    view.findViewById<View>(R.id.recipe_sure).onClickFilter {
      val list =  adapter.getList()
        .filter { it.cnt > 0 }
        .map { Recipe(it.id, it.cnt, it.url) }
      listener?.onDismiss(list)
      dismissAllowingStateLoss()
    }
  }


  private fun initListView(view: RecyclerView) {
    recipeListView.layoutManager = LinearLayoutManager(view.context)
    recipeListView.adapter = adapter

    val allItems = prefabs.map {
      RecipeData(it.id, it.name, 0, it.url)
    }

    data?.forEach { recipe ->
      allItems.firstOrNull {
        it.id == recipe.id
      }?.cnt = recipe.num
    }

    // 排序，数量不为0的放前面
    val chinaCollator = Collator.getInstance(Locale.CHINESE)
    val list = allItems.sortedWith(
      compareByDescending<RecipeData> { it.cnt }.thenBy(chinaCollator) { it.name }
    )
    adapter.addAll(list)
  }

  interface Listener {
    fun onDismiss(items : List<Recipe>)
  }
}


private data class RecipeData(
  val id: String,
  val name: String,
  var cnt: Int = 0,
  var url: String? = null
)

private class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  private val name: TextView = view.findViewById(R.id.recipe_name)
  private val num : EditText = view.findViewById(R.id.recipe_cnt)
  private val icon : ImageView = view.findViewById(R.id.recipe_icon)
  private val add : View = view.findViewById(R.id.recipe_add)
  private val context = view.context

  private var cache : RecipeData? = null
  private var textWatcher = object : TextWatcherAdapter {
    override fun afterTextChanged(s: Editable?) {
      val str = s?.toString()
      if (!str.isNullOrEmpty()) {
        cache?.cnt = str.toInt()
      } else {
        cache?.cnt = 0
      }
    }
  }

  init {
    num.addTextChangedListener(textWatcher)
    add.onClickFilter {
      val cnt = cache?.cnt ?: 0
      cache?.cnt = cnt + 1
      num.setText(cache?.cnt?.toString() ?: "")
    }
  }

  fun bind(item: RecipeData) {
    cache = item

    if (item.url != null) {
      Glide.with(context)
        .load(item.url)
        .into(icon)
    } else {
      Glide.with(context)
        .load("file:///android_asset/icons/${item.id}.png")
        .into(icon)
    }


    name.text = item.name
    if (item.cnt > 0) {
      num.setText(item.cnt.toString())
    } else {
      num.setText("")
    }
  }

  fun unbind() {
    Glide.with(context).clear(icon)
    cache = null
  }
}


private class RecipeAdapter : BaseRecyclerAdapter<RecipeData, RecipeViewHolder>() {

  override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecipeViewHolder {
    return RecipeViewHolder(
      LayoutInflater
        .from(p0.context)
        .inflate(
          R.layout.dst_homepage_recipe_item,
          p0,
          false
        )
    )
  }

  override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
    getItem(position)?.let {
      holder.bind(it)
    }
  }

  override fun onViewRecycled(holder: RecipeViewHolder) {
    super.onViewRecycled(holder)
    holder.unbind()
  }
}