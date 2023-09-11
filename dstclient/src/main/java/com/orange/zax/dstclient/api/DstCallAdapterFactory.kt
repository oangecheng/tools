package com.orange.zax.dstclient.api

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Time: 2023/9/7
 * Author: chengzhi@kuaishou.com
 */
class DstCallAdapterFactory : CallAdapter.Factory() {
  private var lastTime = 0L
  companion object {
    // 请求间隔
    private const val INTERVAL_LIMIT = 500L
  }

  override fun get(
    returnType: Type,
    annotations: Array<out Annotation>,
    retrofit: Retrofit
  ): CallAdapter<*, *> {

    val delegate  = retrofit.nextCallAdapter(this, returnType, annotations)
    return object : CallAdapter<Any, Observable<*>> {
      override fun responseType(): Type {
        return delegate.responseType()
      }

      @Suppress("UNCHECKED_CAST")
      override fun adapt(call: Call<Any>): Observable<*> {
        if (System.currentTimeMillis() - lastTime < INTERVAL_LIMIT) {
          return Observable.just("你操作的太快了，请稍后再试!").doOnNext {
            throw Exception(it)
          }
        }
        val d = delegate as CallAdapter<Any, Observable<*>>
        return d.adapt(call)
          .timeout(5000, TimeUnit.MILLISECONDS)
          .doOnSubscribe {
            lastTime = System.currentTimeMillis()
          }
      }
    }
  }
}