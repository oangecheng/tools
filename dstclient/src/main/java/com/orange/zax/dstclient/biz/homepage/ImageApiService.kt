package com.orange.zax.dstclient.biz.homepage

import com.google.gson.JsonObject
import com.orange.zax.dstclient.api.RetrofitManager
import com.orange.zax.dstclient.biz.homepage.data.ImageResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Time: 2024/12/6
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
interface ImageApiService {

  companion object {
    private val service by lazy {
      RetrofitManager.image(ImageApiService::class.java)
    }

    fun get(): ImageApiService {
      return service
    }
  }

  @FormUrlEncoded
  @POST("1/upload")
  fun upload(
    @Field("key") key : String,
    @Field("title") title: String,
    @Field("source") file: String
  ): Observable<Response<ImageResponse>>
}