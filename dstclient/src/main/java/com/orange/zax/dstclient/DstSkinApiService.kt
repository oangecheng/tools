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
    val service by lazy {
      RetrofitManager.create(DstSkinApiService::class.java)
    }

    fun get(): DstSkinApiService {
      return service
    }
  }

  @FormUrlEncoded
  @POST("/dst/admin/login")
  fun login(
    @Field("username") username: String,
    @Field("password") password: String
  ): Observable<Response<ActionResponse>>


  @FormUrlEncoded
  @POST("/dst/admin/unlockskin")
  fun unlockSkin(
    @Field("username") username: String,
    @Field("password") password: String,
    @Field("userId") userId : String,
    @Field("skinIds") skinIds : String,
    @Field("price") price: Int,
    @Field("extra") extra: String?
    ) : Observable<ActionResponse>


  @FormUrlEncoded
  @POST("/dst/admin/allskin")
  fun querySkinList(
    @Field("username") username: String,
    @Field("password") password: String
  ): Observable<Response<SkinListResponse>>


  @FormUrlEncoded
  @POST("/dst/admin/registerskin")
  fun registerSkin(
    @Field("username") username: String,
    @Field("password") password: String,
    @Field("skinId") id: String,
    @Field("skinName") name: String,
    @Field("skinPrefab") prefab: String,
    @Field("skinType") type: Int,
    @Field("skinPrice") price : Int
  ): Observable<Response<ActionResponse>>


  @FormUrlEncoded
  @POST("/dst/admin/updateskin")
  fun updateSkin(
    @Field("username") username: String,
    @Field("password") password: String,
    @Field("skinId") id: String,
    @Field("skinName") name: String,
    @Field("prefab") prefab: String,
    @Field("skinType") type: Int,
    @Field("skinPrice") price : Int
  ): Observable<Response<ActionResponse>>



  @GET("/dst/admin/alluser")
  fun queryAllUser(
    @Field("username") username: String,
    @Field("password") password: String
  ) : Observable<UserListResponse>


  @FormUrlEncoded
  @POST("/dst/admin/user")
  fun queryUserInfo(
    @Field("username") username: String,
    @Field("password") password: String,
    @Field("userId") id: String
  ): Observable<Response<User>>



  @GET("/test")
  fun test() : Observable<ActionResponse>
}