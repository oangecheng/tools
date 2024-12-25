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
    "这里居住着勤劳而友善的‹›，他们为冒险者们提供了丰富的资源和无私的帮助，投喂相应的饲料可以收获各种类型的产品。" +
      "\n小动物只会在农场范围内(以农场为中心2x2地皮)活动，右键打开面板可以对农场和动物进行升级(需要携带指定物品)。" +
      "\n需要‹饲料盆›和‹灵魂孵化器›才能正常工作，需要先建造农场。" +
      "\n动物: ‹›" +
      "\n数量: " +
      "\n孵化: 分钟" +
      "\n饲料: ‹›‹›‹›‹›" +
      "\n升级: 每次等级+1动物数量上限+50%"+
      "\n设置: mod设置页可配置等级上限和范围"+
      "\n其他: T键建造的农场无法正常工作"
    ,
    "击杀‹›有10%概率掉落蓝图" +
      "\n科技二本‹建筑栏›‹园艺栏›制作"
  )

  var onUseTemplate : ((Template) -> Unit)? = null

  private val foodList = mutableSetOf<String>()
  private val foodListLiveData = MutableLiveData<MutableSet<String>>()

  override fun getLayoutRes(): Int {
    return R.layout.dst_homepage_template_farm
  }

  override fun getStyle(): Int {
    return Style.STYLE_BOTTOM
  }

  override fun getHeight(): Int {
    return ViewUtil.dp2px(600)
  }

  override fun getTitle(): String? {
    return "农场模版"
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val farmDesc = findViewById<EditText>(R.id.farm_desc)
    val farmGain = findViewById<EditText>(R.id.farm_gain_desc)

    val animalName = findViewById<EditText>(R.id.animal_name)
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

    fun descFunc() : String {
      val aName = animalName.text.toString()
      val aNum = animalNum.text.toString().toIntSafe()
      val time = animalTime.text.toString().toIntSafe()
      val food = food()
      return if (aName.isNotEmpty() && aNum != 0 && time != 0 && food.isNotEmpty()) {
        desc(aName) + param(aName, aNum, time) + food + ext()
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

  private fun desc(animalName: String): String {
    return "这里居住着勤劳而友善的‹$animalName›"
  }

  private fun param(animalName: String, animalNum : Int, time : Int) : String {
    return "\n动物: ‹$animalName›" +
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

  private fun ext(): String {
    return "\n升级: 每次等级+1动物数量上限+50%" +
      "\n设置: mod设置页可配置等级上限和范围" +
      "\n其他: T键建造的农场无法正常工作"
  }

  private fun gain(name : String, r : Int) : String {
    return "击杀‹$name›有$r%概率掉落蓝图" +
      "\n科技二本‹建筑栏›‹园艺栏›制作"
  }
}