package com.orange.zax.dstclient.app

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Time: 2023/9/7
 * Author: chengzhi@kuaishou.com
 */
abstract class DstActivity : FragmentActivity() {

  companion object {
    private const val BUNDLE_KEY = "DST_BUNDLE"
  }
  private val disposables = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getLayoutRes())
    val data = intent?.getBundleExtra(BUNDLE_KEY)
    onBind(data)
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.dispose()
  }

  @LayoutRes
  protected abstract fun getLayoutRes(): Int
  protected abstract fun onBind(data: Bundle?)

  protected fun autoDispose(disposable: Disposable) {
    disposables.add(disposable)
  }

  protected fun jump(
    next: Class<out DstActivity>,
    finish: Boolean = false,
    data : Bundle? = null
  ) {
    val intent = Intent()
    data?.let {
      intent.putExtra(BUNDLE_KEY, it)
    }
    intent.setClass(this, next)
    startActivity(intent)
    if (finish) {
      finish()
    }
  }
}