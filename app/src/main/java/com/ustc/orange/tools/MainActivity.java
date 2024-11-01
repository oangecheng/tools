package com.ustc.orange.tools;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustc.orange.tools.test.XBroadcastTest;
import com.ustc.orange.tools.test.XTestModel;
import com.ustc.orange.tools.test.other.AppTestModel;
import com.ustc.orange.tools.test.other.JsonTestModel;
import com.ustc.orange.tools.test.service.ServiceTestModel;
import com.ustc.zax.base.utils.ViewUtil;
import com.ustc.zax.view.spans.GradientColorSpan;
import com.ustc.zax.view.spans.GradientTextSpan;


public class MainActivity extends AppCompatActivity {

  int width = ViewUtil.INSTANCE.dp2px(150);

  private final List<XTestModel> mTestModels = new ArrayList<>();

  @Override
  @SuppressWarnings("unchecked")
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTestModels.add(new JsonTestModel());
    mTestModels.add(new AppTestModel());
    mTestModels.add(new ServiceTestModel());
    mTestModels.add(new XBroadcastTest());

    testFun(1);
    findViewById(R.id.title).setOnClickListener(v -> {
      Intent intent = new Intent(this, ZaxViewActivity.class);
      this.startActivity(intent);
    });

    SpannableStringBuilder builder = new SpannableStringBuilder("aiudnaiounwd");
    builder.append("123\n ssssssss4", new GradientColorSpan("123sssssss", Color.RED, Color.BLUE), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    ((TextView)findViewById(R.id.title)).setText(builder);

    ImageView image = ((ImageView)findViewById(R.id.image_test));

    Bitmap bitmap = new GradientTextSpan().create("哈哈哈哈");
//    image.getLayoutParams().width = bitmap.getWidth();
//    image.getLayoutParams().height = bitmap.getHeight();
    Log.d("orangeLog", "onCreate: bitmap" + bitmap.getWidth());
    BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

    drawable.setTargetDensity(getResources().getDisplayMetrics());
    image.setImageDrawable(drawable);

    testFun(2);

    for(XTestModel model : mTestModels) {
      model.onStart(this);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    for(XTestModel model : mTestModels) {
      model.onStop();
    }
  }

  private void testFun(int a) {
    View add = findViewById(R.id.add);
    View reduce = findViewById(R.id.reduce);

    LiveResizeLogicDemo test = new LiveResizeLogicDemo(findViewById(R.id.live_multi_pk_info_layout));
    reduce.setOnClickListener(v -> {
          width -= 20;
          test.startResize(width);
        }
    );

    add.setOnClickListener(v -> {
          width += 20;
      test.startResize(width);
        }
    );

  }


  public static String getLimitChineseString(@Nullable String text, int maxChineseLength) {
    if (TextUtils.isEmpty(text) || maxChineseLength == 0) {
      return "";
    }

    // 如果文案长度小于等于最大中文长度的1/2，则直接返回
    if (text.length() <= maxChineseLength / 2) {
      return text;
    }

    // 最大字符数限制，由于中文占两字符，要*2
    int maxLength = maxChineseLength * 2;
    int targetTextIndex = 0;
    // 真实长度
    int realLength = 0;

    for (int i = 0; i < text.length(); i++) {
      char item = text.charAt(i);
      // 128为ASCII最大值
      if (item < 128) {
        realLength = realLength + 1;
      } else {
        realLength = realLength + 2;
      }

      if (realLength > maxLength) {
        targetTextIndex = i;
        break;
      }
    }

    // 真实长度小于等于最大长度，则直接返回
    if (realLength <= maxLength) {
      return text;
    }
    return limitTextByMaxLength(text, targetTextIndex);
  }


  public static int codePointsCount(String text) {
    if (TextUtils.isEmpty(text)) {
      return 0;
    }
    return text.codePointCount(0, text.length());
  }

  /**
   * 如果文字中包括emoji等utf-16表示的字符时，要保证完整字符
   *
   * @param text 文本
   * @param maxLength 最大字符长度
   * @return 截取的结果
   */
  public static String limitTextByMaxLength(String text, int maxLength) {
    int codePointsCount = codePointsCount(text);
    if (maxLength < 0) {
      return "";
    }
    if (maxLength >= codePointsCount) {
      return text;
    }
    int endIndex = text.offsetByCodePoints(0, maxLength);
    return text.substring(0, endIndex);
  }


}