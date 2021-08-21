package com.ustc.zax.view.ninepatch

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.NinePatch
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import java.nio.ByteBuffer
import java.nio.ByteOrder

class NinePatchTool {

  /**
   * 从文件路径当中解析.9图片，并转换成[Drawable] 返回
   *
   * @param resources 资源
   * @param filePath  文件路径
   * @return [NinePatchDrawable]
   */
  fun decodeFromPath(
      resources: Resources,
      filePath: String): Drawable? {
    val bitmap = BitmapFactory.decodeFile(filePath) ?: return null
    val chunk = bitmap.ninePatchChunk
    if (!NinePatch.isNinePatchChunk(chunk)) {
      return null
    }
    val decodedChunk = deserialize(chunk) ?: return null
    return NinePatchDrawable(resources, bitmap, chunk, decodedChunk.mPadding, "")
  }

  fun isNinePatchPNG(filePath: String?): Boolean {
    return filePath != null && filePath.endsWith(NINEPATCH_IMAGE_SUFFIX)
  }

  @Throws(IllegalArgumentException::class)
  fun deserialize(data: ByteArray?): Chunk? {
    if (data == null) return null

    val byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.nativeOrder())
    if (byteBuffer.get().toInt() == 0) {
      return null // is not serialized
    }
    val chunk = Chunk()
    chunk.mDivX = IntArray(byteBuffer.get().toInt())
    chunk.mDivY = IntArray(byteBuffer.get().toInt())
    chunk.mColor = IntArray(byteBuffer.get().toInt())
    val illegal = checkDivCount(chunk.mDivX.size) && checkDivCount(chunk.mDivY.size)
    if (!illegal) {
      return null
    }

    // skip 8 bytes
    byteBuffer.int
    byteBuffer.int
    chunk.mPadding.left = byteBuffer.int
    chunk.mPadding.right = byteBuffer.int
    chunk.mPadding.top = byteBuffer.int
    chunk.mPadding.bottom = byteBuffer.int

    // skip 4 bytes
    byteBuffer.int
    readIntArray(chunk.mDivX, byteBuffer)
    readIntArray(chunk.mDivY, byteBuffer)
    readIntArray(chunk.mColor, byteBuffer)
    return chunk
  }

  private fun readIntArray(data: IntArray, buffer: ByteBuffer) {
    var i = 0
    val n = data.size
    while (i < n) {
      data[i] = buffer.int
      ++i
    }
  }

  private fun checkDivCount(length: Int): Boolean {
    return length != 0 && length and 0x01 == 0
  }

  /**
   * 封装.9 chunk的信息
   */
  inner class Chunk {
    val mPadding = Rect()
    lateinit var mDivX: IntArray
    lateinit var mDivY: IntArray
    lateinit var mColor: IntArray
  }

  companion object {
    private const val NINEPATCH_IMAGE_SUFFIX = ".9.png"
  }
}