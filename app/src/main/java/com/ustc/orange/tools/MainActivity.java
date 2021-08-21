package com.ustc.orange.tools;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.title).setOnClickListener(v -> {
      Intent intent = new Intent(this, ZaxViewActivity.class);
      this.startActivity(intent);
    });
  }
}