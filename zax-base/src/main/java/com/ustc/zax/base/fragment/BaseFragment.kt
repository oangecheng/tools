package com.ustc.zax.base.fragment

import android.view.View
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Time: 2024/12/2
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
open class BaseFragment : Fragment() {

  private val disposables by lazy {
    CompositeDisposable()
  }

  protected fun autoDispose(disposable: Disposable) {
    disposables.add(disposable)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    disposables.dispose()
  }


  fun <T : View> findViewById(id : Int) : T {
    return view?.findViewById(id)!!
  }
}