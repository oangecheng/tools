package com.orange.zax.dstclient.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Time: 2023/9/7
 * Author: chengzhi@kuaishou.com
 */
abstract class DstActivity : Activity() {

  private val disposables = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getLayoutRes())
    onBizInit()
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.dispose()
  }

  @LayoutRes
  protected abstract fun getLayoutRes(): Int
  protected abstract fun onBizInit()

  protected fun autoDispose(disposable: Disposable) {
    disposables.add(disposable)
  }

  protected fun jump(
    next: Class<DstActivity>,
    finish: Boolean = false
  ) {
    val intent = Intent()
    intent.setClass(this, next)
    startActivity(intent)
    if (finish) {
      finish()
    }
  }
}