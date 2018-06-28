package com.znkj.rchl_hz.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.getInfoUtils;

public class JwhcActivity extends AppCompatActivity implements View.OnClickListener{

    private Button backBtn;
    private Button jnhcBtn;
    private Button addBtn;
    private TextView title;
    private TextView bottom;

    private EditText gjdq;
    private EditText zjzl;
    private EditText zjhm;

    private getInfoUtils getInfo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jwhc_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initView(){
        title = (TextView)findViewById(R.id.text_title);
        title.setText("境外人员");
        bottom = (TextView)findViewById(R.id.bottom_address);
        bottom.setText(getInfo.getAddress(this));
        gjdq = (EditText)findViewById(R.id.jwhc_gjdq);
        zjzl = (EditText)findViewById(R.id.jwhc_zjzl);
        zjhm = (EditText)findViewById(R.id.jwhc_zjhm);
        backBtn = findViewById(R.id.back_btn);
        addBtn = findViewById(R.id.jw_add_btn);
        jnhcBtn = findViewById(R.id.jw_btn);
        Drawable drawable = getDrawable(R.drawable.ic_jn);
        jnhcBtn.setBackground(drawable);
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        jnhcBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.back_btn:
                JwhcActivity.this.finish();
                break;
            case R.id.jw_btn:
                Intent intent = new Intent(JwhcActivity.this,MidActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.jw_add_btn:
                //暂时屏蔽
                //SubmitInfo();
                Intent intent1 = new Intent(JwhcActivity.this,
                        ResultActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    public void SubmitInfo() {

        if (gjdq.getText().toString().trim().equalsIgnoreCase("")
                || zjzl.getText().toString().trim().equalsIgnoreCase("")
                || zjhm.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(JwhcActivity.this, "您输入的信息不完整！",
                    Toast.LENGTH_SHORT).show();
        } else {

            try {
                HcInfo hcInfo = new HcInfo();
                hcInfo.setHclx("jw");
                hcInfo.setHcbb(getInfo.getUserInfo(this,"sys_bb"));
                hcInfo.setHcr_xm(getInfo.getUserInfo(this,"use_name"));
                hcInfo.setHcr_sjh(getInfo.getUserInfo(this,"use_phonenum"));
                hcInfo.setHcr_sfzh(getInfo.getUserInfo(this,"use_idcard"));
                hcInfo.setHcdz(getInfo.getAddress(this));

                //String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
                HttpUtil.sendHcRequest(this.getString(R.string.url_address), hcInfo, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Message message = new Message();
                        message.obj = response;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Exception e) {
                        Message message = new Message();
                        message.obj = e.toString();
                        mHandler.sendMessage(message);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result;

            if ("OK".equals(msg.obj.toString())){
                result = "提交成功！";
            }else if ("Wrong".equals(msg.obj.toString())){
                result = "提交失败！";
            }else {
                result = msg.obj.toString();
            }
            Toast.makeText(JwhcActivity.this, result, Toast.LENGTH_LONG).show();
        }
    };
}
