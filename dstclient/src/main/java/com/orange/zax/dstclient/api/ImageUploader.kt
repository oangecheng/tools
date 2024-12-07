package com.orange.zax.dstclient.api

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.orange.zax.dstclient.biz.homepage.ImageApiService
import com.ustc.zax.base.GlobalConfig
import com.ustc.zax.base.log.XLog
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.apache.commons.io.FileUtils
import java.io.File


/**
 * Time: 2024/12/6
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class ImageUploader(private val frg: Fragment) {

  companion object {
    private const val PICK_IMAGE = 100
    private const val KEY = "chv_4Llj_83f405a4fe9c1c4e26b7bf3673f7bee4659416513434526d1f3a956c3e8e456defde04cea0700f053458dd1ed6783c8680530b9c348b57e7649419bf2a7f3862"
  }

  init {
    if (ContextCompat.checkSelfPermission(
        frg.requireActivity(),
        Manifest.permission.READ_EXTERNAL_STORAGE
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      ActivityCompat.requestPermissions(
        frg.requireActivity(),
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
        100
      )
    }
  }


  fun open() {
    // 在你希望启动相册的地方，比如一个按钮的点击事件中
    val intent =
      Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    frg.startActivityForResult(intent, PICK_IMAGE)
  }

  fun onSelect(uri: Uri, id: String, success: (String) -> Unit) {
    XLog.info("ceshi", "uri = $uri")
    try {
      val isFile = GlobalConfig.application.contentResolver.openInputStream(uri)
      val bytes = isFile?.readBytes()
      val b = Base64.encodeToString(bytes, Base64.DEFAULT)

      val d = ImageApiService.get().upload(KEY, id, b)
        .observeOn(AndroidSchedulers.mainThread())
        .map { it.body()!! }
        .subscribe({
          XLog.info("ceshi", "上传成功 $it")
          success(it.image.url)
        }, {
          XLog.error("ceshi", "上传失败", it)
        })
    } catch (e : Exception) {
      XLog.error("ceshi", "上传失败", e)

    }


  }
}