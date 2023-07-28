package com.ustc.zax.service.example

/**
 * Time: 2023/7/28
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
interface Service {
  fun <T : Service> bind(target: T)
}


interface LivePkService : Service {
  fun show(tip : String) : Boolean
}


class LivePkServiceImpl : LiveBaseBizService<LivePkService>(), LivePkService {
  override fun show(tip: String): Boolean {
    return invoke("1", false) {
      show(tip)
    }
  }
}


abstract class LiveBaseBizService<S: Service> : Service {
  private var service: Service? = null

  override fun <T : Service> bind(target: T) {
    service = target
  }

  fun <R> invoke(name: String, defVal: R, func: S.() -> R): R {
    val s = service ?: return defVal
    return (s as S).func()
  }
}


