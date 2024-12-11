package com.orange.zax.dstclient.biz.homepage

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.bumptech.glide.Glide
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ImageUploader
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.biz.homepage.data.ItemCache
import com.orange.zax.dstclient.biz.homepage.data.ItemType
import com.orange.zax.dstclient.biz.homepage.data.Prefab
import com.orange.zax.dstclient.utils.TextWatcherAdapter
import com.orange.zax.dstclient.utils.ToastUtil
import com.ustc.zax.base.fragment.BaseFragment

/**
 * Time: 2024/12/2
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
open class PageBase : BaseFragment() {

  companion object {
    private const val MIX = 1
    private const val DROP = 2

    private val TECHS = mapOf(
      "合成" to MIX,
      "掉落" to DROP
    )
  }


  private lateinit var etId: EditText
  private lateinit var etDesc: EditText
  private lateinit var etName: EditText
  private lateinit var etGain: EditText
  private lateinit var image : EditText
  private lateinit var imagePre : ImageView

  private lateinit var spTech: Spinner
  private lateinit var btnTab: Button
  private lateinit var tvTab : TextView
  private lateinit var btnRecipe: Button

  private lateinit var tvRecipe : TextView
  protected val itemInfoCache = ItemData.mock()
  protected lateinit var btnAction: Button

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(
      R.layout.dst_homepage_layout,
      container,
      false
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    findViewById<TextView>(R.id.title).text = title()
    initViews()
  }

  private fun <T : View> findViewById(id : Int) : T {
    return view?.findViewById(id)!!
  }

  private fun initViews() {
    etId = findViewById(R.id.input_id)
    etName = findViewById(R.id.input_name)
    etDesc = findViewById(R.id.input_desc)
    etGain = findViewById(R.id.input_gain)
    image = findViewById(R.id.input_image)
    imagePre = findViewById(R.id.image_preview)

    etId.addTextChangedListener(object : TextWatcherAdapter {
      override fun afterTextChanged(s: Editable?) {
        itemInfoCache.id = s.toString().trim()
      }
    })

    etName.addTextChangedListener(object : TextWatcherAdapter {
      override fun afterTextChanged(s: Editable?) {
        itemInfoCache.name = s.toString()
      }
    })

    etDesc.addTextChangedListener(object : TextWatcherAdapter {
      override fun afterTextChanged(s: Editable?) {
        itemInfoCache.desc = s.toString()
      }
    })

    etGain.addTextChangedListener(object : TextWatcherAdapter {
      override fun afterTextChanged(s: Editable?) {
        itemInfoCache.gain = s.toString()
      }
    })

    spTech = findViewById(R.id.spinner_tech)
    btnTab = findViewById(R.id.btn_tabs)
    tvTab = findViewById(R.id.tv_tabs)
    btnRecipe = findViewById(R.id.btn_recipes)
    tvRecipe = findViewById(R.id.tv_recipes)
    btnAction = findViewById(R.id.btn_action)

    val adapter = ArrayAdapter<Tech>(requireContext(), R.layout.support_simple_spinner_dropdown_item)
    val techs = TECHS.map { Tech(it.value, it.key) }.sortedBy { it.tech }
    adapter.addAll(techs)
    spTech.adapter = adapter
    spTech.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        itemInfoCache.gainType = techs[position].tech
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    btnAction.onClickFilter {
      itemInfoCache.image = image.text.toString().trim()
      if (itemInfoCache.id.isNotEmpty()) {
        onAction(itemInfoCache)
      }
    }

    btnRecipe.onClickFilter {
      HomeRecipeDialog.instance(
        itemInfoCache.recipes,
        object : HomeRecipeDialog.Listener {
          override fun onDismiss(items: List<Recipe>) {
            itemInfoCache.recipes = items.toMutableList()
            setRecipe(itemInfoCache.recipes)
          }
        }
      ).show(
        childFragmentManager,
        "ss"
      )
    }


    btnTab.onClickFilter {
      HomeTabSelectDialog.newInstance(
        itemInfoCache.tabs,
        object : HomeTabSelectDialog.Listener {
          override fun onDismiss(tabs: List<Int>) {
            itemInfoCache.tabs.clear()
            itemInfoCache.tabs.addAll(tabs)
            updateTabText(itemInfoCache.tabs)
          }
        }
      ).show(
        childFragmentManager,
        "sss"
      )
    }

    findViewById<View>(R.id.input_image_url_clear).setOnClickListener {
      image.setText("")
    }

    findViewById<View>(R.id.input_image_url_select).setOnClickListener {
      ImageDialog.instance(object : ImageDialog.Listener {
        override fun onSelected(prefab: Prefab) {
          image.setText(prefab.url)
          Glide.with(this@PageBase).load(prefab.url).into(imagePre)
        }
      }).show(childFragmentManager, "")
    }

  }

  protected open fun onAction(data: ItemData) {

  }

  protected open fun title() : String {
    return "标题"
  }


  protected fun updatePage(data: ItemData) {
    etId.setText(data.id)
    etName.setText(data.name)
    etDesc.setText(data.desc)
    etGain.setText(data.gain)
    image.setText(data.image)
    spTech.setSelection(data.gainType - 1)
    updateTabText(data.tabs)
    setRecipe(data.recipes)
    Glide.with(this@PageBase).load(data.image).into(imagePre)
  }

  private fun updateTabText(tabs : List<Int>?) {
    tvTab.text = tabs?.map { TABS[it] }?.joinToString("|")
  }

  private fun setRecipe(recipes: List<Recipe>?) {
    tvRecipe.text = recipes?.joinToString("|") {
      val p = ItemCache.item(ItemType.RECIPE, it.id)
      "${p?.name}x${it.num}"
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
}