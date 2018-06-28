package com.znkj.rchl_hz.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.znkj.rchl_hz.R;
import com.githang.statusbar.StatusBarCompat;

import org.json.JSONObject;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

public class ResultActivity extends AppCompatActivity {

    private Button endBtn;
    //private TextView title;
    private ImageView resImg;
    private ExpandableTextView bjxx;
    private TextView hcr;
    private TextView hcrxb;
    private TextView hcrsfzh;
    private TextView hcrmz;
    private TextView hcrcsrq;
    private TextView hcrhjdz;

    private String res_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);
        initView();
        getData();
        Intent intent= getIntent();
        res_str = intent.getStringExtra("result");

        endBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  ResultActivity.this.finish();
              }
        });

    }

    private void initView(){
        endBtn = (Button)findViewById(R.id.end_btn);
        resImg = (ImageView)findViewById(R.id.res_img);
        bjxx = (ExpandableTextView)findViewById(R.id.etv);
        hcr = (TextView)findViewById(R.id.hcjg_xm);
        hcrxb = (TextView)findViewById(R.id.hcjg_xb);
        hcrsfzh = (TextView)findViewById(R.id.hcjg_sfzh);
        hcrmz = (TextView)findViewById(R.id.hcjg_mz);
        hcrcsrq = (TextView)findViewById(R.id.hcjg_csrq);
        hcrhjdz = (TextView)findViewById(R.id.hcjg_hjdz);
    }

    private void getData(){
        Intent intent= getIntent();
        res_str = intent.getStringExtra("result");
        //JSONObject object;
        //object = JSONObject.fromObject(res_str);
    }

}
