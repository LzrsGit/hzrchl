package com.znkj.rchl_hz.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.znkj.rchl_hz.R;
import com.githang.statusbar.StatusBarCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

public class ClhcResultActivity extends AppCompatActivity {

    private Button endBtn;
    private TextView title;

    private ImageView resImg;
    private ExpandableTextView bjxx;
    private TextView clbh;
    private TextView cllx;
    private TextView clhm;

    private String res_str;
    private List resinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clhc_result_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);
        getData();
        initView();
        endBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(ClhcResultActivity.this,
                          ClhcActivity.class);
                  startActivity(intent);
                  ClhcResultActivity.this.finish();
              }
        });
        if(resinfo!=null){
            if(!("000".equalsIgnoreCase((String) resinfo.get(0)))){
                Toast.makeText(ClhcResultActivity.this, "返回值错误，错误代码："+ resinfo.get(0), Toast.LENGTH_LONG).show();
                ClhcResultActivity.this.finish();
            }else {
                setData();
            }
        }

    }

    public void initView(){
        endBtn = (Button) findViewById(R.id.end_btn);
        resImg = (ImageView)findViewById(R.id.res_img);
        bjxx = (ExpandableTextView)findViewById(R.id.cl_xx);
        clbh = (TextView)findViewById(R.id.clhcjg_clbh);
        cllx = (TextView)findViewById(R.id.clhcjg_cllx);
        clhm = (TextView)findViewById(R.id.clhcjg_cphm);
    }

    private void setData(){
        clbh.setText((String)resinfo.get(2));
        cllx.setText((String)resinfo.get(3));
        clhm.setText((String)resinfo.get(4));
        bjxx.setText((String)resinfo.get(7));
        if(!"".equalsIgnoreCase(((String)resinfo.get(5)).trim())&&resinfo.get(5)!=null){
            if("通过".equalsIgnoreCase((String)resinfo.get(5))){
                resImg.setImageResource(R.drawable.green_cl_icon);
                bjxx.setTextColor(Color.parseColor("#8cc542") );
            }else if("存疑".equalsIgnoreCase((String)resinfo.get(5))){
                resImg.setImageResource(R.drawable.yellow_cl_icon);
                bjxx.setTextColor(Color.parseColor("#f4b251") );
            }else if("拦截".equalsIgnoreCase((String)resinfo.get(5))){
                resImg.setImageResource(R.drawable.red_cl_icon);
                bjxx.setTextColor(Color.parseColor("#f25c44") );
            }
        }

    }

    private void getData(){
        Intent intent= getIntent();
        res_str = intent.getStringExtra("result");
        JSONObject Jsobject = null;
        String resArr = null;
        JSONArray jsArr = null;
        try {
            Jsobject = new JSONObject(res_str);
        }catch (JSONException e){
            e.printStackTrace();
        }
        if(Jsobject!=null){
            //Object res;
            //Log.e("Jsobject"," is not null");
            try{
                jsArr = (JSONArray) Jsobject.get("res");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        if (jsArr!=null){
            for (int i=0;i<jsArr.length();i++){
                JSONArray tempArr = null;
                if(i==1) {
                    try {
                        tempArr = (JSONArray) jsArr.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (tempArr!=null) {
                    resinfo = new ArrayList();
                    for (int j = 0; j <tempArr.length(); j++) {
                        try {
                            resArr =  URLDecoder.decode((String)tempArr.get(j),"UTF-8");
                            //URLDecoder.decode(obj.getString("updateDesc"), "UTF-8");
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        //Log.e("resArr[" + i + "][" + j + "]===", resArr);
                        resinfo.add(resArr);
                    }
                }
            }
        }

    }

}
