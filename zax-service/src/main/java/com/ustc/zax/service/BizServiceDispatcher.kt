package com.ustc.zax.service

/**
 * Time: 2023/7/28
 * Author: chengzhi@kuaishou.com
 */
interface BizServiceDispatcher {

  fun <T:Listener> register(t : T)
  fun <T:Listener> unregister(t : T)


}

interface Listener {}


class Dispatcher : BizServiceDispatcher {
  private val listeners = HashMap<Class<out Listener>, MutableList<Listener>>()

  override fun <T : Listener> register(t: T) {
    val list = listeners[t.javaClass]?:ArrayList()
    list.add(t)
    
    listeners[t.javaClass] = list
  }

  override fun <T : Listener> unregister(t: T) {
   listeners[t.javaClass]?.remove(t)
  }
}