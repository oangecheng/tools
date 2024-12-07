package com.orange.zax.dstclient.api

import com.orange.zax.dstclient.biz.homepage.ImageApiService
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HostnameVerifier

/**
 * Time: 2023/9/7
 * Author: chengzhi@kuaishou.com
 */
object RetrofitManager {
  private const val PATH = "dst/admin/"
  private const val BASE_URL = "https://www.orangezax.cn/"
  private const val TEST_URL = "https://172.16.80.65:8080/"

  private const val PAGE_PATH = "dst/hpy/page/"

  private val httpClient by lazy {
    // 信任所有域名，因为没什么安全问题
    OkHttpClient.Builder()
      .hostnameVerifier(HostnameVerifier { _, _ -> true })
      .addInterceptor(DstRequestInterceptor())
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

  private val retrofit2 by lazy {
    Retrofit.Builder()
      .addCallAdapterFactory(DstCallAdapterFactory())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .addConverterFactory(GsonConverterFactory.create())
      .callbackExecutor {  }
      .baseUrl(BASE_URL + PAGE_PATH)
      .client(httpClient)
      .build()
  }

  private val retrofitImage by lazy {
    val client = OkHttpClient.Builder()
      .hostnameVerifier(HostnameVerifier { _, _ -> true })
      .build()

    Retrofit.Builder()
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .addConverterFactory(GsonConverterFactory.create())
      .callbackExecutor {  }
      .baseUrl("https://www.imagehub.cc/api/")
      .client(client)
      .build()
  }


  fun <T> create(service : Class<T>) : T {
    return retrofit.create(service)
  }


  fun <T> create2(service : Class<T>) : T {
    return retrofit2.create(service)
  }

  fun <T> image(service: Class<T>) : T {
    return retrofitImage.create(service)
  }


  private fun baseUrl(): String {
    return if (TestConfig.isTest()) {
      TEST_URL + PATH
    } else {
      BASE_URL + PATH
    }
  }
}