package com.orange.zax.dstclient

import com.orange.zax.dstclient.api.ActionResponse
import com.orange.zax.dstclient.api.Response
import com.orange.zax.dstclient.api.RetrofitManager
import com.orange.zax.dstclient.data.SkinListResponse
import com.orange.zax.dstclient.data.User
import com.orange.zax.dstclient.data.UserListResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 */
interface DstSkinApiService {

  companion object {
    private val service by lazy {
      RetrofitManager.create(DstSkinApiService::class.java)
    }

    fun get(): DstSkinApiService {
      return service
    }
  }

  @FormUrlEncoded
  @POST("login")
  fun login(
    @Field("username") username: String,
    @Field("password") password: String
  ): Observable<Response<ActionResponse>>


  @FormUrlEncoded
  @POST("unlockskin")
  fun unlockSkin(
    @Field("userId") userId : String,
    @Field("skinIds") skinIds : String,
    @Field("price") price: Int,
    @Field("extra") extra: String?
    ) : Observable<ActionResponse>


  @POST("allskin")
  fun querySkinList(): Observable<Response<SkinListResponse>>


  @FormUrlEncoded
  @POST("registerskin")
  fun registerSkin(
    @Field("skinId") id: String,
    @Field("skinName") name: String,
    @Field("prefab") prefab: String,
    @Field("skinType") type: Int,
    @Field("skinPrice") price : Int
  ): Observable<Response<ActionResponse>>


  @FormUrlEncoded
  @POST("updateskin")
  fun updateSkin(
    @Field("skinId") id: String,
    @Field("skinName") name: String,
    @Field("prefab") prefab: String,
    @Field("skinType") type: Int,
    @Field("skinPrice") price : Int
  ): Observable<Response<ActionResponse>>


  @FormUrlEncoded
  @POST("user")
  fun queryUserInfo(
    @Field("userId") id: String
  ): Observable<Response<User>>


  @FormUrlEncoded
  @POST("giverole")
  fun giveUserRole(
    @Field("userId") id: String,
    @Field("role") role : Int
  ) : Observable<ActionResponse>


  @GET("/test")
  fun test() : Observable<ActionResponse>
}