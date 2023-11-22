package com.orange.zax.dstclient.pages

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.orange.zax.dstclient.DstSkinApiService
import com.orange.zax.dstclient.R
import com.orange.zax.dstclient.api.ErrorConsumer
import com.orange.zax.dstclient.api.ResponseFunction
import com.orange.zax.dstclient.app.DstActivity
import com.orange.zax.dstclient.app.onClickFilter
import com.orange.zax.dstclient.utils.DstAlert
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * Time: 2023/10/24
 * Author: chengzhi@kuaishou.com
 */
class DstBillActivity : DstActivity() {

  companion object {
    private const val DAY_TIME = DateUtils.DAY_IN_MILLIS
    private const val DAYS = 7
  }


  override fun getLayoutRes(): Int {
    return R.layout.dst_bill_layout
  }

  override fun onBind(data: Bundle?) {
    val spinner = findViewById<Spinner>(R.id.bill_date)
    val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item)
    val dates = getDates(DAYS)
    adapter.addAll(dates.keys.toMutableList().sortedDescending())
    spinner.adapter = adapter

    findViewById<View>(R.id.query).onClickFilter {
      val item = spinner.selectedItem as String
      dates[item]?.let { day ->
        DstSkinApiService.get()
          .queryBill(day)
          .map(ResponseFunction())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            DstAlert.alert(this, "当天收入: ￥${it.amount}")
          }, { ErrorConsumer().accept(it)
          })
      }
    }
  }

  private fun getDates(days: Int): Map<String, Int> {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val time = System.currentTimeMillis()
    val map = HashMap<String, Int>()
    for (i in 1..days) {
      val date = format.format(time - DAY_TIME * i)
      map[date] = i
    }
    return map
  }
}