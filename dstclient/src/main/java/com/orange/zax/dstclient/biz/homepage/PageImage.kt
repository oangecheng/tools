package com.orange.zax.dstclient.biz.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.biz.homepage.data.Prefab
import com.orange.zax.dstclient.biz.homepage.data.XSp
import com.ustc.zax.base.fragment.BaseFragment

/**
 * Time: 2024/12/3
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
class PageImage : BaseFragment() {

  companion object {
    private const val KEY = "PREFABS"

    fun instance() : BaseFragment {
      return PageImage()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(
      R.layout.dst_homepage_image,
      container,
      false
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)


    val vId = view.findViewById<EditText>(R.id.input_image_id)
    val vName = view.findViewById<EditText>(R.id.input_image_name)
    val vUrl = view.findViewById<EditText>(R.id.input_image_url)
    val vBtn =  view.findViewById<View>(R.id.btn_sure)

    vBtn.setOnClickListener {
      val id = vId.text.toString().trim()
      val name = vName.text.toString().trim()
      val url = vUrl.text.toString().trim()
      XSp.addPrefab(Prefab(id, name, url))
    }
  }

}