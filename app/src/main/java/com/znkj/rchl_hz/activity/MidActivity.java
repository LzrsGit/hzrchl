package com.znkj.rchl_hz.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.adapter.MyFragmentPagerAdapter;
import com.znkj.rchl_hz.utils.getInfoUtils;
import com.githang.statusbar.StatusBarCompat;


public class MidActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private getInfoUtils getInfo;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TextView title;
    private Button backBtn;
    private Button jwhcBtn;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //getSupportActionBar().hide();//隐藏掉整个ActionBar
        setContentView(R.layout.mid_layout);
        //设置状态栏颜色
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);
        //初始化视图
        initViews();

    }

    private void initViews() {
        title = (TextView)findViewById(R.id.text_title);
        title.setText("境内人员");
        address = (TextView)findViewById(R.id.bottom_address);
        address.setText(getInfo.getAddress(this));
        backBtn = (Button)findViewById(R.id.back_btn);
        jwhcBtn = (Button)findViewById(R.id.jw_btn);
        backBtn.setOnClickListener(this);
        jwhcBtn.setOnClickListener(this);

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.back_btn:
                MidActivity.this.finish();
                break;
            case R.id.jw_btn:
                Intent intent = new Intent(MidActivity.this,JwhcActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}