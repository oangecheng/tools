package com.ustc.orange.tools;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ustc.zax.base.GlobalConfig;
import com.ustc.zax.base.utils.ViewUtil;
import com.ustc.zax.service.BizPresenter;
import com.ustc.zax.service.example.LiveTestBizPresenter2;
import com.ustc.zax.service.example.LiveBizScopes;
import com.ustc.zax.service.example.LiveTestBizPresenter1;
import com.ustc.zax.service.example.LiveTestBizService;
import com.ustc.zax.service.manager.BizServiceCenter;
import com.ustc.zax.tool.JsonUtils;
import com.ustc.zax.tool.SystemUtils;

public class MainActivity extends AppCompatActivity {

  int width = ViewUtil.INSTANCE.dp2px(150);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    testFun(1);
    findViewById(R.id.title).setOnClickListener(v -> {
      Intent intent = new Intent(this, ZaxViewActivity.class);
      this.startActivity(intent);
    });

    testFun(2);

    JsonUtils utils = new JsonUtils();
//    utils.test();

    SystemUtils.INSTANCE.lunchApp(
      this,
      "https://autoriza-pagamento.caixa.gov.br/openbankingibc"
    );

    testService();

  }

  private void testService() {
    BizPresenter presenter = new BizPresenter() {
      @Override
      public int scope() {
        return LiveBizScopes.BASE;
      }
    };
    BizServiceCenter center = new BizServiceCenter();
    center.register(LiveTestBizService.class, new LiveTestBizService() {});

    presenter.addPresenter(new LiveTestBizPresenter1());
    presenter.addPresenter(new LiveTestBizPresenter2());
    presenter.create(center);
    presenter.bind();
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