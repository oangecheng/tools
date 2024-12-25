package com.orange.zax.dstclient.biz.homepage.data


/**
 * Time: 2024/12/25
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
private val FARM = Template(
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


private val SOUL = Template(
  "蕴含着强大的生命能量，放入‹农场›的‹灵魂孵化器›中，可孕育出‹›",
  "科技二本‹合成栏›制作"
)


private val ANIM = Template(
  "一只会打篮球的鸡，生活在‹农场›中，消耗饲料生产物品。" +
    "\n产物: *1(权重9) *1(权重9)" +
    "\n消耗: 饲料*2" +
    "\n耗时: 20分钟" +
    "\n升级: 等级+1生产提速5%" +
    "\n变异: ‹蛋鸡›‹肉鸡›‹› "
  ,
  "使用‹灵魂›孵化获得"
)


class Template(val desc: String, val gain: String) {
  companion object {
    fun get(type: Int): Template {
      return when (type) {
        1 -> FARM
        2 -> SOUL
        3 -> ANIM
        else -> throw IllegalArgumentException("error type")
      }
    }
  }
}