package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.orange.zax.dialogs.XDialog
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.biz.homepage.data.Template
import com.ustc.zax.base.utils.ViewUtil

/**
 * Time: 2024/12/25
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */

fun String.toIntSafe(): Int {
  return try {
    this.toInt()
  } catch (e: Exception) {
    0
  }
}

class DialogTemplateFarm : XDialog() {

  companion object {
    fun instance() : DialogTemplateFarm {
      return DialogTemplateFarm()
    }
  }

  private val farm = Template(
    "这里居住着勤劳而友善的‹›" +
      "\n动物: ‹›" +
      "\n数量: " +
      "\n孵化: 分钟" +
      "\n饲料: ‹›‹›‹›‹›"
    ,
    "击杀‹›有10%概率掉落蓝图，科技二本‹建筑栏›‹园艺栏›制作"
  )

  var onUseTemplate : ((Template) -> Unit)? = null

  private val foodList = mutableSetOf<String>()
  private val foodListLiveData = MutableLiveData<MutableSet<String>>()

  private val animList = mutableSetOf<String>()
  private val animListLiveData = MutableLiveData<MutableSet<String>>()

  override fun getLayoutRes(): Int {
    return R.layout.dst_homepage_template_farm
  }

  override fun getStyle(): Int {
    return Style.STYLE_BOTTOM
  }

  override fun getHeight(): Int {
    return ViewUtil.dp2px(700)
  }

  override fun getTitle(): String? {
    return "农场模版"
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val farmDesc = findViewById<EditText>(R.id.farm_desc)
    val farmGain = findViewById<EditText>(R.id.farm_gain_desc)

    val animalName = findViewById<EditText>(R.id.animal_name)
    val animalAdd = findViewById<View>(R.id.animal_add)
    val animalClear = findViewById<View>(R.id.animal_clear)
    val animalDesc = findViewById<TextView>(R.id.animal_desc)

    val animalNum = findViewById<EditText>(R.id.animal_cnt)
    val animalTime = findViewById<EditText>(R.id.animal_time)

    val victim = findViewById<EditText>(R.id.victim_name)
    val ratio = findViewById<EditText>(R.id.drop_ratio)

    val foodName = findViewById<EditText>(R.id.food_name)
    val foodAdd = findViewById<View>(R.id.food_add)
    val foodClear = findViewById<View>(R.id.food_clear)
    val foodDesc = findViewById<TextView>(R.id.food_desc)

    foodAdd.setOnClickListener {
      val name = foodName.text.toString().trim()
      if (!TextUtils.isEmpty(name)) {
        foodList.add(name)
        foodName.text.clear()
        foodListLiveData.value = foodList
      }
    }

    foodClear.setOnClickListener {
      foodList.clear()
      foodListLiveData.value = foodList
    }


    foodListLiveData.observe(this, Observer {
      it.joinToString("|").let { str ->
        foodDesc.text = str
      }
    })


    animalAdd.setOnClickListener {
      val name = animalName.text.toString().trim()
      if (!TextUtils.isEmpty(name)) {
        animList.add(name)
        animalName.text.clear()
        animListLiveData.value = animList
      }
    }

    animalClear.setOnClickListener {
      animList.clear()
      animListLiveData.value = foodList
    }


    animListLiveData.observe(this, Observer {
      it.joinToString("|").let { str ->
        animalDesc.text = str
      }
    })


    fun descFunc() : String {
      val aNum = animalNum.text.toString().toIntSafe()
      val time = animalTime.text.toString().toIntSafe()
      val food = food()
      return if (animList.isNotEmpty() && aNum != 0 && time != 0 && food.isNotEmpty()) {
        desc() + param(animList.toList(), aNum, time) + food
      } else {
        farm.desc
      }
    }

    fun gainFunc() : String {
      val n = victim.text.toString()
      val r = ratio.text.toString().toIntSafe()
      return if (n.isNotEmpty() && r > 0) {
        gain(n, r)
      } else {
        farm.gain
      }
    }

    findViewById<View>(R.id.sure).setOnClickListener {
      onUseTemplate?.invoke(
        Template(
          farmDesc.text.toString(), farmGain.text.toString()
        )
      )
    }

    findViewById<View>(R.id.preview).setOnClickListener {
      farmDesc.setText(descFunc())
      farmGain.setText(gainFunc())
    }

  }

  private fun desc(): String {
    return "这里居住着勤劳而友善的"
  }

  private fun param(animalName: List<String>, animalNum : Int, time : Int) : String {
    val str = animalName.joinToString("›‹", "‹", "›")
    return "\n动物: $str" +
      "\n数量: $animalNum" +
      "\n孵化: ${time}分钟"
  }

  private fun food(): String {
    if (foodList.isEmpty()) {
      return ""
    }
    val s = StringBuilder("\n饲料: ")
    foodList.forEach {
      s.append("‹$it›")
    }
    return s.toString()
  }

  private fun gain(name : String, r : Int) : String {
    return "击杀‹$name›有$r%概率掉落蓝图，科技二本‹建筑栏›‹园艺栏›制作"
  }
}