package com.orange.zax.dstclient.app

import android.view.View
import com.orange.zax.dstclient.view.ClickFilter
import io.reactivex.disposables.Disposable

/**
 * Time: 2023/9/7
 * Author: chengzhi@kuaishou.com
 */

@JvmSynthetic
fun Disposable.auto(): Disposable {
  return this
}

@JvmSynthetic
fun View.onClickFilter(cb : (v:View)->Unit) {
  setOnClickListener(ClickFilter(cb))
}