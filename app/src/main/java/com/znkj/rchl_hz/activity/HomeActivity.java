package com.znkj.rchl_hz.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.MainActivity;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.WelcomeActivity;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.getInfoUtils;
import com.githang.statusbar.StatusBarCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private CoordinatorLayout right;
    private NavigationView left;
    private boolean isDrawer=false;
    private ImageView ryhc;
    private ImageView clhc;
    private ImageView zhhc;
    private AutoCompleteTextView hcdd;
    private LinearLayout getFocus;
    private getInfoUtils getInfo;
    private int infoInt = 0;
    private ImageView img ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.home_main);

        //获取用户信息
        String u_name = "";
        String u_id = "";
        String s_bb = "";
        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        u_name = sp.getString("use_name", "");
        u_id = sp.getString("use_id", "");
        s_bb = sp.getString("sys_bb", "02");
        img = findViewById(R.id.main_img);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(s_bb.equalsIgnoreCase("01")){
            img.setImageResource(R.drawable.ab);
            img.setBackgroundColor(Color.parseColor("#00db43"));
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#00db43"), false);
        }else{
            img.setImageResource(R.drawable.lm);
            img.setBackgroundColor(Color.parseColor("#1FA0FE"));
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE"), false);
        }

        hcdd = (AutoCompleteTextView)findViewById(R.id.hcdd);
        if(!(getInfo.getAddress(this)==null||getInfo.getAddress(this).trim().equalsIgnoreCase(""))){
            hcdd.setText(getInfo.getAddress(this));
        }
        getFocus = (LinearLayout)findViewById(R.id.getfocus);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initAutoComplete("history",hcdd);
        //键盘完成点击事件
        hcdd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(  actionId == EditorInfo.IME_ACTION_DONE){
                    if(hcdd.getText().toString().trim().length()<=4){
                        Toast.makeText(HomeActivity.this, "核查地点应大于四个字", Toast.LENGTH_LONG).show();
                        clearAddress();
                    }else {
                        saveAddress("address",hcdd);
                        //saveHistory("history",hcdd);
                    }
                    hcdd.clearFocus();
                    //输入框失焦
                    getFocus.setFocusable(true);
                    getFocus.setFocusableInTouchMode(true);
                    getFocus.requestFocus();
                    getFocus.requestFocusFromTouch();
                    //隐藏键盘
                    imm.hideSoftInputFromWindow(getFocus.getWindowToken(), 0);
                }
                return false;
            }
        });

        //文本加粗
        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView1.getHeaderView(0);
        TextView tv = (TextView)headerLayout.findViewById(R.id.user_name);
        tv.setText("姓名："+u_name);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        TextView tv1 = (TextView)headerLayout.findViewById(R.id.user_id);
        tv1.setText("警号："+u_id);
        TextPaint tp1 = tv1.getPaint();
        tp1.setFakeBoldText(true);

        ryhc = (ImageView)findViewById(R.id.ryhc);
        clhc = (ImageView)findViewById(R.id.clhc);
        zhhc = (ImageView)findViewById(R.id.zhhc);
        ryhc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hcdd.getText().toString().trim().length()<=4){
                    Toast.makeText(HomeActivity.this, "核查地点应大于四个字", Toast.LENGTH_LONG).show();
                    clearAddress();
                }else {
                    saveAddress("address",hcdd);
                    //saveHistory("history",hcdd);
                    Intent intent = new Intent(HomeActivity.this,
                            MidActivity.class);
                    startActivity(intent);
                }
            }
        });
        clhc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hcdd.getText().toString().trim().length()<=4){
                    Toast.makeText(HomeActivity.this, "核查地点应大于四个字", Toast.LENGTH_LONG).show();
                    clearAddress();
                }else {
                    saveAddress("address",hcdd);
                    //saveHistory("history",hcdd);
                    Intent intent = new Intent(HomeActivity.this,
                            ClhcActivity.class);
                    startActivity(intent);
                }
            }
        });
        zhhc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hcdd.getText().toString().trim().length()<=4){
                    Toast.makeText(HomeActivity.this, "核查地点应大于四个字", Toast.LENGTH_LONG).show();
                    clearAddress();
                }else {
                    saveAddress("address",hcdd);
                    //saveHistory("history",hcdd);
                    Intent intent = new Intent(HomeActivity.this,
                            ZhhcActivity.class);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        right = (CoordinatorLayout) findViewById(R.id.right);
        left = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isDrawer){
                    return left.dispatchTouchEvent(motionEvent);
                }else{
                    return false;
                }
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer=true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                right.layout(left.getRight(), 0, left.getRight() + display.getWidth(), display.getHeight());
            }
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer=false;
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        img = findViewById(R.id.main_img);

        if (id == R.id.nav_hc) {
            // Handle the camera action
            if (img==null){
                Toast.makeText(HomeActivity.this, "null", Toast.LENGTH_LONG).show();
            }else{
                SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("sys_bb", "02");
                editor.commit();
                img.setImageResource(R.drawable.lm);
                img.setBackgroundColor(Color.parseColor("#1FA0FE"));
                findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor("#1FA0FE"));
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE"), false);
            }

        } else if (id == R.id.nav_ab) {
            if (img==null){
                Toast.makeText(HomeActivity.this, "null", Toast.LENGTH_LONG).show();
            }else{
                String zt = "0";
                String dateStr ="";
                JSONObject dateJson = null;
                JSONObject tempJson = null;
                JSONArray jsArr = null;
                dateStr = getInfo.getUserInfo(this,"abqx");
                if(!"".equalsIgnoreCase(dateStr.trim())){
                    try {
                        dateJson = new JSONObject(dateStr);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {Toast.makeText(HomeActivity.this, "安保权限获取错误", Toast.LENGTH_LONG).show();}
                if(dateJson!=null){
                    try {
                        //Log.e("jjjjjjjjj","==="+dateJson.get("res"));
                        jsArr = (JSONArray) dateJson.get("res");
                        for (int j=0; j<jsArr.length(); j++){
                            tempJson = (JSONObject)jsArr.get(j);

                            //Log.e("ssss","==="+tempJson.get("name"));
                            //Log.e("ssss","==="+tempJson.get("dm"));
                            try {
                                zt = ""+ URLDecoder.decode((String)tempJson.get("zt"),"UTF-8");
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                if(!("0".equalsIgnoreCase(zt))){
                    SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("sys_bb", "01");
                    editor.commit();
                    img.setImageResource(R.drawable.ab);
                    img.setBackgroundColor(Color.parseColor("#00db43"));
                    findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor("#00db43"));
                    StatusBarCompat.setStatusBarColor(this, Color.parseColor("#00db43"), false);
                }else {
                    Toast.makeText(HomeActivity.this, "暂无安保版权限，请联系管理员", Toast.LENGTH_LONG).show();
                }

            }

        }
//        else if (id == R.id.nav_xl) {
//            if (img==null){
//                Toast.makeText(HomeActivity.this, "null", Toast.LENGTH_LONG).show();
//            }else{
//                SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("sys_bb", "41");
//                editor.commit();
//                img.setImageResource(R.drawable.lm);
//                img.setBackgroundColor(Color.parseColor("#1FA0FE"));
//                findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor("#1FA0FE"));
//                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE"), false);
//            }
//
//        }
        else if (id == R.id.nav_download) {
            getData("hjd");
            getData("gjdq");
            getData("zjzl");
        }else if (id == R.id.quit_app) {
            infoInt = 1;
            showDialog("确认注销当前用户吗？");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 初始化AutoCompleteTextView，最多显示5项提示，使
     * AutoCompleteTextView在一开始获得焦点时自动提示
     * @param field 保存在sharedPreference中的字段名
     * @param auto 要操作的AutoCompleteTextView
     */
    private void initAutoComplete(String field,AutoCompleteTextView auto) {
//        SharedPreferences sp = getSharedPreferences("addressHis", MODE_PRIVATE);
//        String longhistory = sp.getString("history", "");
        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);

        //获取核查地点
        JSONObject dateJson = null;
        JSONObject tempJson = null;
        JSONArray jsArr = null;
        String dataStr = sp.getString("hcdd", "");
        String longhistory = "";
        if(!"".equalsIgnoreCase(dataStr.trim())){
            try {
                dateJson = new JSONObject(dataStr);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        if(dateJson!=null){
            try {
                //Log.e("jjjjjjjjj","==="+dateJson.get("res"));
                jsArr = (JSONArray) dateJson.get("res");
                for (int j=0; j<jsArr.length(); j++){
                    tempJson = (JSONObject)jsArr.get(j);
                    try {
                        longhistory = ""+URLDecoder.decode((String)tempJson.get("hcdd"),"UTF-8");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        String[]  hisArrays = longhistory.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.auto_search_item, hisArrays);
        //只保留最近的50条的记录
//        if(hisArrays.length > 50){
//            String[] newArrays = new String[50];
//            System.arraycopy(hisArrays, 0, newArrays, 0, 50);
//            adapter = new ArrayAdapter<String>(this,
//                    R.layout.auto_search_item, newArrays);
//        }
        auto.setAdapter(adapter);
        auto.setDropDownHeight(550);
        auto.setThreshold(1);
        //auto.setCompletionHint("最近的50条记录");
        //auto.sethi(Color.parseColor("#333333"));
    }

    //取消保存历史地址功能，地址改为字典项，从后台获取
    private void saveHistory(String field,AutoCompleteTextView auto) {
        String text = auto.getText().toString();
        SharedPreferences sp = getSharedPreferences("addressHis", MODE_PRIVATE);
        String longhistory = sp.getString(field, "");
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).commit();
        }
    }

    private void saveAddress(String name,AutoCompleteTextView auto){
        String add = auto.getText().toString();
        SharedPreferences sp = getSharedPreferences(name, MODE_PRIVATE);
        sp.edit().putString("add", add).commit();
    }

    private void clearAddress(){
        hcdd.setText("");
        SharedPreferences sp = getSharedPreferences("address", MODE_PRIVATE);
        sp.edit().putString("add", "").commit();
    }

    private void showDialog(String info){
        // 创建退出对话框
        AlertDialog isExit = new AlertDialog.Builder(this).create();
        //设置点击其他地方对话框不消失
        isExit.setCanceledOnTouchOutside(false);
        // 设置对话框标题
        isExit.setTitle("提示");
        // 设置对话框消息
        isExit.setMessage(info);
        // 添加选择按钮并注册监听
        isExit.setButton("确定", listener);
        isExit.setButton2("取消", listener);
        // 显示对话框
        isExit.show();
        isExit.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        isExit.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }
    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出登录
                    if (1==infoInt) {               //注销用户
                        getInfo.delUseInfo(HomeActivity.this);
                        Intent intent = new Intent(HomeActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                    }
                    infoInt = 0;
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    infoInt = 0;
                    break;
                default:
                    break;
            }
        }
    };

    //back键监听
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            infoInt = 2;
            showDialog("确认退出人车核录系统吗？");
        }
        return false;
    }


    //下载字典项
    private void getData(String sType){
        HcInfo hcInfo = new HcInfo();
        hcInfo.setSjlx(sType);
        HttpUtil.sendHcRequest(this.getString(R.string.url_address), hcInfo, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = new Message();
                if ("WRONG".equalsIgnoreCase(response.trim())) {
                    message.what = 0;
                } else {
                    if (sType.equalsIgnoreCase("hjd")){
                        message.what = 1;
                    }else if(sType.equalsIgnoreCase("gjdq")){
                        message.what = 2;
                    }else if(sType.equalsIgnoreCase("zjzl")){
                        message.what = 3;
                    }else {
                        message.what = 4;
                    }
                    message.obj = response;
                }
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Message message = new Message();
                message.what = 0;
                message.obj = e.toString();
                mHandler.sendMessage(message);
            }
        });
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            String DmArr = "";
            switch (msg.what) {
                case 1:  //户籍地
                    DmArr = msg.obj.toString();
                    //Log.e("darr","1====="+DmArr);
                    editor.putString("hjdList", DmArr);
                    Toast.makeText(HomeActivity.this, "获取字典项成功", Toast.LENGTH_LONG).show();
                    editor.commit();
                    break;
                case 2:  //国籍地区
                    DmArr = msg.obj.toString();
                    //Log.e("darr","2====="+DmArr);
                    editor.putString("gjdqList", DmArr);
                    Toast.makeText(HomeActivity.this, "获取字典项成功", Toast.LENGTH_LONG).show();
                    editor.commit();
                    break;
                case 3:  //证件类型
                    DmArr = msg.obj.toString();
                    //Log.e("darr","3====="+DmArr);
                    editor.putString("zjzlList", DmArr);
                    Toast.makeText(HomeActivity.this, "获取字典项成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:  //数据错误
                    Toast.makeText(HomeActivity.this, "获取字典项类型错误", Toast.LENGTH_LONG).show();
                    editor.putString("hjdList", "");
                    editor.putString("gjdqList", "");
                    editor.putString("zjzlList", "");
                    break;
                case 0:
                    //String result = msg.obj.toString();
                    String result = "更新字典项失败，请稍后重试";
                    //Log.e("bbbbuuuuuggggg","====="+msg.obj.toString());
                    Toast.makeText(HomeActivity.this, result, Toast.LENGTH_LONG).show();
                    editor.putString("hjdList", "");
                    editor.putString("gjdqList", "");
                    editor.putString("zjzlList", "");
                    break;
                default:
                    break;
            }
            editor.commit();
        }
    };

}
