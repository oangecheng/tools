package android.view

import android.annotation.SuppressLint

/**
 * Time: 2024/11/25
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class ListenerChecker {




  @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
  private fun getOnClickListenerV14(view: View): View.OnClickListener? {
    var retrievedListener: View.OnClickListener? = null
    val viewStr = "android.view.View"
    val lInfoStr = "android.view.View\$ListenerInfo"

    try {
      val listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo")
      var listenerInfo: Any? = null

      if (listenerField != null) {
        listenerField.isAccessible = true
        listenerInfo = listenerField[view]
      }

      val clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnClickListener")

      if (clickListenerField != null && listenerInfo != null) {
        retrievedListener = clickListenerField[listenerInfo] as View.OnClickListener
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return retrievedListener
  }
}