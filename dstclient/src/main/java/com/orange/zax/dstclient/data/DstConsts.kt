package com.orange.zax.dstclient.data

/**
 * Time: 2024/6/7
 * Author: chengzhi@kuaishou.com
 */



private val ITEMS = mapOf(
  // Special
  "zxstone" to "海琉璃",
  "sanity" to "精神值",

  // B
  "bearger_fur" to "厚重的皮毛",
  "berries" to "浆果",
  "bird_egg" to "鸟蛋",
  "blue_cap" to "蓝蘑菇",
  "bluegem" to "蓝宝石",
  "boards" to "木板",
  "butterfly" to "蝴蝶",

  // C
  "corn" to "玉米",
  "coontail" to "浣猫尾",
  "cutgrass" to "割下的草",
  "cutstone" to "石砖",

  // D
  "dug_berrybush" to "浆果丛",

  // F
  "fireflies" to "萤火虫",

  // G
  "gears" to "齿轮",
  "goatmilk" to "羊奶",
  "goldenaxe" to "黄金斧头",
  "goldenshovel" to "黄金铲子",
  "goldnugget" to "金块",
  "green_cap" to "绿蘑菇",

  // H
  "horn" to "野牛角",

  // L
  "lightninggoathorn" to "电羊角",
  "livinglog" to "活木",
  "log" to "木头",

  // M
  "monstermeat" to "怪物肉",
  "moon_tree_blossom" to "月树花",

  // N
  "nightmarefuel" to "噩梦燃料",

  // P
  "petals" to "花瓣",
  "pigskin" to "猪皮",
  "pinecone" to "松果",

  // R
  "redgem" to "红宝石",
  "red_cap" to "红蘑菇",
  "redgem" to "红宝石",
  "rocks" to "石头",
  "rope" to "绳子",

  // S
  "silk" to "蜘蛛丝",
  "spidereggsack" to "蜘蛛卵",

  // T
  "tallbirdegg" to "高脚鸟蛋",
  "trunk_winter" to "冬日象鼻",
  "trunk_summer" to "夏日象鼻",
  "twigs" to "小树枝",

  // Y
  "yellowgem" to "黄宝石"
)


fun getRecipeItems(): Map<String, String> {
  return ITEMS
}