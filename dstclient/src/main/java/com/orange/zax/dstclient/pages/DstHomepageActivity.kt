package com.orange.zax.dstclient.pages

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.google.gson.Gson
import com.orange.zax.dstclient.PageApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.biz.homepage.HomeRecipeDialog
import com.orange.zax.dstclient.biz.homepage.HomeTabSelectDialog
import com.orange.zax.dstclient.biz.homepage.TABS
import com.orange.zax.dstclient.data.ItemInfo
import com.orange.zax.dstclient.data.Recipe
import com.orange.zax.dstclient.data.getRecipeItems
import com.orange.zax.dstclient.utils.TextWatcherAdapter
import com.orange.zax.dstclient.utils.ToastUtil
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Time: 2024/5/29
 * Author: chengzhi@kuaishou.com
 */
class DstHomepageActivity : DstActivity() {

  companion object {
    private const val TECH_SI_1 = 1
    private const val TECH_SI_2 = 2
    private const val TECH_MG_1 = 3
    private const val TECH_MG_2 = 4

    private val TECHS = mapOf(
      "科技1本" to TECH_SI_1,
      "科技2本" to TECH_SI_2,
      "魔法1本" to TECH_MG_1,
      "魔法2本" to TECH_MG_2
    )
  }

  private lateinit var etId: EditText
  private lateinit var etDesc: EditText
  private lateinit var etName: EditText
  private lateinit var etGain: EditText
  private lateinit var spTech: Spinner
  private lateinit var btnTab: Button
  private lateinit var tvTab : TextView
  private lateinit var btnRecipe: Button
  private lateinit var tvRecipe : TextView
  private lateinit var btnSearch: Button
  private lateinit var btnUpdate : Button

  private var itemInfoFromServer : ItemInfo? = null
  private val itemInfoCache = ItemInfo.mock()

  override fun getLayoutRes(): Int {
    return R.layout.dst_homepage_layout
  }


  override fun onBind(data: Bundle?) {
    initViews()
  }

  private fun initViews() {
    etId = findViewById(R.id.input_id)
    etName = findViewById(R.id.input_name)
    etDesc = findViewById(R.id.input_desc)
    etGain = findViewById(R.id.input_gain)

    etId.addTextChangedListener(object : TextWatcherAdapter {
      override fun afterTextChanged(s: Editable?) {
        itemInfoCache.id = s.toString().trim()
      }
    })

    etName.addTextChangedListener(object: TextWatcherAdapter {
      override fun afterTextChanged(s: Editable?) {
        itemInfoCache.name = s.toString()
      }
    })

    etDesc.addTextChangedListener(object: TextWatcherAdapter {
      override fun afterTextChanged(s: Editable?) {
        itemInfoCache.desc = s.toString()
      }
    })

    etGain.addTextChangedListener(object: TextWatcherAdapter {
      override fun afterTextChanged(s: Editable?) {
        itemInfoCache.gain = s.toString()
      }
    })

    spTech = findViewById(R.id.spinner_tech)
    btnTab = findViewById(R.id.btn_tabs)
    tvTab = findViewById(R.id.tv_tabs)
    btnRecipe = findViewById(R.id.btn_recipes)
    tvRecipe = findViewById(R.id.tv_recipes)
    btnSearch = findViewById(R.id.btn_search)
    btnUpdate = findViewById(R.id.btn_update)

    val adapter = ArrayAdapter<Tech>(this, R.layout.support_simple_spinner_dropdown_item)
    val techs = TECHS.map { Tech(it.value, it.key) }.sortedBy { it.tech }
    adapter.addAll(techs)
    spTech.adapter = adapter
    spTech.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        itemInfoCache.tech = techs[position].tech
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }


    btnSearch.onClickFilter {
      etId.text.toString().let {
        if (it.isNotEmpty()) {
          queryItem(it)
        }
      }
    }

    btnUpdate.onClickFilter { _ ->
      val i = itemInfoFromServer
      if (i != null) {
        updateItem(itemInfoCache)
      } else {
        addNewItem(itemInfoCache)
      }
    }

    btnTab.onClickFilter {
      HomeTabSelectDialog.newInstance(
        itemInfoCache.tabs,
        object : HomeTabSelectDialog.Listener {
          override fun onDismiss(tabs: List<Int>) {
            itemInfoCache.tabs.clear()
            itemInfoCache.tabs.addAll(tabs)
            setTab(itemInfoCache.tabs)
          }
        }
      ).show(
        supportFragmentManager,
        "sss"
      )
    }

    btnRecipe.onClickFilter {
      Log.d("orangeTest", "recipe click ${itemInfoCache.recipes}")
      HomeRecipeDialog.instance(
        itemInfoCache.recipes,
        object : HomeRecipeDialog.Listener {
          override fun onDismiss(items: List<Recipe>) {
            itemInfoCache.recipes = items.toMutableList()
            setRecipe(itemInfoCache.recipes)
          }
        }
      ).show(
        supportFragmentManager,
        "ss"
      )
    }
  }

  private fun queryItem(id : String) {
    PageApiService.get()
      .queryItem(id)
      .observeOn(AndroidSchedulers.mainThread())
      .map(ResponseFunction())
      .subscribe(
        {
          etId.setText(it.id)
          etName.setText(it.name)
          etDesc.setText(it.desc)
          etGain.setText(it.gain)
          spTech.setSelection(it.tech - 1)

          itemInfoFromServer = it

          btnUpdate.text = "更新"

          ItemInfo.copy(it, itemInfoCache)
          setRecipe(itemInfoCache.recipes)
          setTab(itemInfoCache.tabs)
          Log.d("orangeTest", "$itemInfoCache")

        },
        {
          ErrorConsumer().accept(it)
        }
      ).also {
        autoDispose(it)
      }
  }

  private fun updateItem(itemInfo: ItemInfo) {
    if (!checkItem(itemInfo)) {
      return
    }
    PageApiService.get().updateItem(
      itemInfo.id,
      Gson().toJson(itemInfo)
    )
      .observeOn(AndroidSchedulers.mainThread())
      .map(ResponseFunction())
      .subscribe(
        {
          ToastUtil.showShort("更新成功")
        },
        {
          ErrorConsumer().accept(it)
        }
      ).also {
        autoDispose(it)
      }
  }

  private fun addNewItem(itemInfo: ItemInfo) {
    if (!checkItem(itemInfo)) {
      return
    }

    PageApiService.get().addItem(
      itemInfo.id,
      Gson().toJson(itemInfo)
    )
      .observeOn(AndroidSchedulers.mainThread())
      .map(ResponseFunction())
      .subscribe(
        {
          ToastUtil.showShort("新增物品成功")
        },
        {
          ErrorConsumer().accept(it)
        }
      ).also {
        autoDispose(it)
      }
  }

  private fun setTab(tabs : List<Int>?) {
    tvTab.text = tabs?.map { TABS[it] }?.joinToString("|")
  }

  private val items = getRecipeItems()
  private fun setRecipe(recipes: List<Recipe>?) {
    tvRecipe.text = recipes?.map {
     "${items[it.id]}x${it.num}"
    }?.joinToString(" | ")
  }

  private fun checkItem(target : ItemInfo) : Boolean {
    return target.id.isNotEmpty() && target.name.isNotEmpty()
  }
}

private data class Tech(
  val tech: Int,
  val name: String
) {
  override fun toString(): String {
    return name
  }
}

