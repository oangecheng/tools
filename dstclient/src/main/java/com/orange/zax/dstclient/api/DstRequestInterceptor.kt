package com.orange.zax.dstclient.api

import com.orange.zax.dstclient.AdminAccount
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Time: 2023/10/9
 * Author: chengzhi@kuaishou.com
 *
 */
class DstRequestInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val builder = request.newBuilder()
    if ("POST".equals(request.method, true) && AdminAccount.name().isNotEmpty()) {
      val body = request.body
      if (body is FormBody || body == null || body.contentLength() == 0L) {
        val formBodyBuilder = FormBody.Builder()
          .add("username", AdminAccount.name())
          .add("password", AdminAccount.pwd())
        if (body is FormBody && body.size > 0) {
          for (i in 0 until body.size) {
            formBodyBuilder.add(body.name(i), body.value(i))
          }
        }
        builder.method(
          request.method,
          formBodyBuilder.build()
        )
      }
    }
    return chain.proceed(builder.build())
  }
}