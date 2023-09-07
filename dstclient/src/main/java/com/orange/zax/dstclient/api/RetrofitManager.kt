package com.orange.zax.dstclient.api

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HostnameVerifier

/**
 * Time: 2023/9/7
 * Author: chengzhi@kuaishou.com
 */
object RetrofitManager {
  private const val BASE_URL = "https://43.138.31.203:8081/"
  private const val TEST_URL = "https://172.23.56.60:8080/"

  private val httpClient by lazy {
    // 信任所有域名，因为没什么安全问题
    OkHttpClient.Builder()
      .hostnameVerifier(HostnameVerifier { _, _ -> true })
      .build()
  }

  private val retrofit by lazy {
    Retrofit.Builder()
      .addCallAdapterFactory(DstCallAdapterFactory())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .addConverterFactory(GsonConverterFactory.create())
      .callbackExecutor {  }
      .baseUrl(baseUrl())
      .client(httpClient)
      .build()
  }


  fun <T> create(service : Class<T>) : T {
    return retrofit.create(service)
  }


  private fun baseUrl(): String {
    return if (TestConfig.isTest()) {
      TEST_URL
    } else {
      BASE_URL
    }
  }
}