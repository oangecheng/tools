package com.orange.zax.dstclient.pages

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.google.gson.Gson
import com.orange.zax.dstclient.PageApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.data.ItemInfo
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
  private lateinit var btnRecipe: Button
  private lateinit var btnSearch: Button
  private lateinit var btnUpdate : Button

  private var itemInfo : ItemInfo? = null

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
    spTech = findViewById(R.id.spinner_tech)
    btnTab = findViewById(R.id.btn_tabs)
    btnRecipe = findViewById(R.id.btn_recipes)
    btnSearch = findViewById(R.id.btn_search)
    btnUpdate = findViewById(R.id.btn_update)

    val adapter = ArrayAdapter<Tech>(this, R.layout.support_simple_spinner_dropdown_item)
    adapter.addAll(TECHS.map {
      Tech(it.value, it.key)
    }.sortedBy {
      it.tech
    })
    spTech.adapter = adapter


    btnSearch.onClickFilter {
      etId.text.toString().let {
        if (it.isNotEmpty()) {
          queryItem(it)
        }
      }
    }

    btnUpdate.onClickFilter { _ ->
      val i = itemInfo
      if (i != null) {
        updateItem(i)
      } else {

      }
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
          itemInfo = it
        }, {
          ErrorConsumer().accept(it)
        }
      ).also {
        autoDispose(it)
      }
  }

  private fun updateItem(itemInfo: ItemInfo) {
    val name = getText(etName)
    val desc = getText(etDesc)
    val id = getText(etId)
    val gain = getText(etGain)
    val tech = (spTech.selectedItem as Tech).tech
    val recipe = itemInfo.recipes
    val tabs = itemInfo.tabs

    PageApiService.get().updateItem(
      id,
      Gson().toJson(
        ItemInfo(
          id, name, desc, tabs, tech, gain, recipe
        )
      )
    ).subscribe {
      ToastUtil.showShort("更新成功")
    }.also {
      autoDispose(it)
    }

  }

  private fun getText(et: EditText): String {
    return et.text.toString().trim()
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

private data class RecipeItem(
  val name: String,
  val isMode : Boolean = false
)


private val RECIPE_DATA = mapOf(
  "twigs" to RecipeItem("小树枝"),
  "cutgrass" to RecipeItem("干草"),
  "log" to RecipeItem("木头"),
  "boards" to RecipeItem("木板"),
  "rocks" to RecipeItem("石头")
)
