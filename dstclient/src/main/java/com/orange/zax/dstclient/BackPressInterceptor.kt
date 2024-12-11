package com.orange.zax.dstclient

/**
 * Time: 2024/12/11
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
interface BackPressInterceptor {
  fun intercept() : Boolean
}