package com.ustc.zax.service.biz

/**
 * Time: 2023/7/27
 * Author: chengzhi@kuaishou.com
 */

interface LiveBiz {
  val subBiz : List<LiveBiz>?
  val priority : Int
  val parent : LiveBiz?
  fun registerSubBiz(biz: LiveBiz) {}
}


abstract class LiveBaseBiz(private val parentBiz: LiveBiz?) : LiveBiz {
  private val subBizList = ArrayList<LiveBiz>()

  init {
    parentBiz?.registerSubBiz(this)
  }

  override val subBiz: List<LiveBiz>?
    get() = subBizList
  override val parent: LiveBiz?
    get() = parentBiz

  override fun registerSubBiz(biz: LiveBiz) {
    subBizList.add(biz)
  }
}


class LiveBizPkInvite(private val parentBiz : LiveBiz) : LiveBaseBiz(parentBiz) {

  override val subBiz: List<LiveBiz>?
    get() = null
  override val priority: Int
    get() = parentBiz.priority + 1
  override val parent: LiveBiz
    get() = parentBiz
}


class LiveBizPkMatch(private val parentBiz : LiveBiz) : LiveBiz {
  override val subBiz: List<LiveBiz>?
    get() = null
  override val priority: Int
    get() = parentBiz.priority + 1
  override val parent: LiveBiz
    get() = parentBiz
}


class LiveBizPk: LiveBiz {
  private val subBizList = ArrayList<LiveBiz>()

  override fun registerSubBiz(biz: LiveBiz) {
    subBizList.add(biz)
  }

  override val subBiz: List<LiveBiz>
    get() = subBizList
  override val priority: Int
    get() = 100
  override val parent: LiveBiz?
    get() = null
}


class Test {

  private val liveBiz = ArrayList<LiveBiz>()

  init {
    val bizPkBase : LiveBiz = LiveBizPk()
    val bizInvite : LiveBiz = LiveBizPkInvite(bizPkBase)
    val bizMatch  : LiveBiz = LiveBizPkMatch(bizPkBase)
    bizPkBase.registerSubBiz(bizInvite)
    bizPkBase.registerSubBiz(bizMatch)
  }

  fun enableBiz(biz: LiveBiz) {
    liveBiz.firstOrNull {
      it == biz
    }?.let {
      if (it.parent != null) {

      }
    }
  }
}