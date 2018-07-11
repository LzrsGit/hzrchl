package com.znkj.rchl_hz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.znkj.rchl_hz.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.cybertech.pdk.UserInfo;

public class WelcomeActivity extends Activity {

	private Toast toast;
	private void showToast(String message) {
		if (toast != null) {
			toast.cancel();
			toast = null;
		}
		toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.show();
	}

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_welcome);
		getUserInfo();
		if(checkInfo()){
			enterMain();
		}else{
			//initView();
			showToast("未获取到有效的用户信息");
			WelcomeActivity.this.finish();
		}
	}

	//跳转登录页面
	private void initView(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(WelcomeActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 2000);
	}

	//跳转首页
	private void enterMain(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(WelcomeActivity.this,
						HomeActivity.class);
				startActivity(intent);
				finish();
			}

		}, 0);
	}

	//验证用户信息
	private boolean checkInfo(){
		boolean ok=false;
		String u_name = "";
		String u_id = "";
		SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
		u_name = sp.getString("use_name", "");
		u_id = sp.getString("use_idcard", "");
		if (!(u_name.trim().equalsIgnoreCase(""))&&!(u_id.trim()
				.equalsIgnoreCase(""))){
			ok = true;
		}
		return ok;
	}

	private void getUserInfo(){
		String raw = UserInfo.getUserRawData(WelcomeActivity.this);
		if (raw!=null&&(!"".equalsIgnoreCase(raw.trim()))) {
			try {
				JSONObject userinfo = new JSONObject(raw);
				// 将json对象根据key(person)，拿到对应的value(Person对象)值
				SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("use_name", userinfo.getString("realname"));
				editor.putString("use_idcard", userinfo.getString("idcard"));
				editor.putString("use_dw", userinfo.getString("deptName"));
				editor.putString("use_phonenum", userinfo.getString("phone"));
				editor.putString("sys_bb", "02");//默认路面版
				editor.commit();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("use_name", "didiasid");
			editor.putString("use_idcard", "123123123");
			editor.putString("use_dw", "ddwwwdddwww");
			editor.putString("use_phonenum", "11122233");
			editor.putString("sys_bb", "02");//默认路面版
			editor.commit();
		}
	}

}
