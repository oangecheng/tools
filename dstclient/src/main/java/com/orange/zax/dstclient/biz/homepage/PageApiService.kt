package com.orange.zax.dstclient.biz.homepage

import com.orange.zax.dstclient.api.ActionResponse
import com.orange.zax.dstclient.api.Response
import com.orange.zax.dstclient.api.RetrofitManager
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Time: 2024/5/28
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
interface PageApiService {

  companion object {
    private val service by lazy {
      RetrofitManager.create2(PageApiService::class.java)
    }

    fun get(): PageApiService {
      return service
    }
  }

  @GET("items/all")
  fun getAllItems(): Observable<Response<ItemResponse>>


  @FormUrlEncoded
  @POST("items/search")
  fun queryItem(@Field("id") id : String): Observable<Response<ItemInfo>>

  @FormUrlEncoded
  @POST("items/add")
  fun addItem(
    @Field("id") id: String,
    @Field("data") data: String
  ): Observable<Response<ActionResponse>>

  @FormUrlEncoded
  @POST("items/update")
  fun updateItem(@Field("id") id: String, @Field("data") data: String):
    Observable<Response<ActionResponse>>
}