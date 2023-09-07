package com.orange.zax.dstclient

import com.orange.zax.dstclient.utils.Utils

/**
 * Time: 2023/9/5
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
object AdminAccount {
  var adminName : String? = null
  var adminPwd : String? = null

  fun name() : String {
    return Utils.emptyIfNull(adminName)
  }

  fun pwd() : String {
    return Utils.emptyIfNull(adminPwd)
  }
}