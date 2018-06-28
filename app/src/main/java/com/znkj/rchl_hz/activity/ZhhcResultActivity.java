package com.znkj.rchl_hz.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.znkj.rchl_hz.R;
import com.githang.statusbar.StatusBarCompat;

public class ZhhcResultActivity extends AppCompatActivity {

    private Button endBtn;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhhc_result_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);

        initView();
        endBtn = findViewById(R.id.end_btn);
        endBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  ZhhcResultActivity.this.finish();
              }
        });

    }

    public void initView(){

    }

}
