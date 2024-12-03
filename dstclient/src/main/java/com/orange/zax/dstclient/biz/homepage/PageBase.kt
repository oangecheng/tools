package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.utils.TextWatcherAdapter
import com.ustc.zax.base.fragment.BaseFragment

/**
 * Time: 2024/12/2
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
open class PageBase : BaseFragment() {

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
        itemInfoCache.tech = techs[position].tech
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    btnAction.onClickFilter {
      onAction(itemInfoCache)
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
    spTech.setSelection(data.tech - 1)
    updateTabText(data.tabs)
    setRecipe(data.recipes)
  }

  private fun updateTabText(tabs : List<Int>?) {
    tvTab.text = tabs?.map { TABS[it] }?.joinToString("|")
  }

  private val items = getRecipeItems()
  private fun setRecipe(recipes: List<Recipe>?) {
    tvRecipe.text = recipes?.map {
      "${items[it.id]}x${it.num}"
    }?.joinToString(" | ")
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