package com.ustc.orange.tools;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.ustc.orange.annotation.ZaxLog;

@ZaxLog
public class MainActivity extends AppCompatActivity {

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
  }


  private void testFun(int a) {

  }
}