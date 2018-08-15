package com.znkj.rchl_hz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.znkj.rchl_hz.activity.HomeActivity;
import com.znkj.rchl_hz.activity.ResultActivity;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.model.PersonBean;
import com.znkj.rchl_hz.utils.DialogUtils;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.getInfoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.com.cybertech.pdk.UserInfo;

public class WelcomeActivity extends Activity {

	private List personBeans;
	private String DmArr="";

	private getInfoUtils getInfo;

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
		String raw = null;
		try{
			UserInfo.getUserRawData(WelcomeActivity.this);
		}catch (Exception e){
			e.printStackTrace();
		}
		if (raw!=null&&(!"".equalsIgnoreCase(raw.trim()))) {
			try {
				JSONObject userinfo = new JSONObject(raw);
				// 将json对象根据key(person)，拿到对应的value(Person对象)值

				String hjddate = getInfo.getUserInfo(this, "hjdList");
				String gjdqdate = getInfo.getUserInfo(this, "gjdqList");
				String zjzldate = getInfo.getUserInfo(this, "zjzlList");
				String hcdddate = getInfo.getUserInfo(this, "hcdd");
				if("".equalsIgnoreCase(hjddate)||hjddate==null){
					getData("hjd");
				}
				if("".equalsIgnoreCase(gjdqdate)||gjdqdate==null){
					getData("gjdq");
				}
				if("".equalsIgnoreCase(zjzldate)||zjzldate==null){
					getData("zjzl");
				}
				if("".equalsIgnoreCase(hcdddate)||hcdddate==null){
					getData("hcdd");//获取核查地点
				}
				SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("use_name", userinfo.getString("realname"));
				editor.putString("use_idcard", userinfo.getString("idcard"));
				editor.putString("use_dw", userinfo.getString("deptName"));
				editor.putString("use_phonenum", userinfo.getString("phone"));
				editor.putString("sys_bb", "02");//默认路面版
				editor.putString("abqx", "0");//安保版权限，0无权限，1有权限，默认无权限
				editor.commit();
				getData("abqx");//获取安保权限

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			String hjddate = getInfo.getUserInfo(this, "hjdList");
			String gjdqdate = getInfo.getUserInfo(this, "gjdqList");
			String zjzldate = getInfo.getUserInfo(this, "zjzlList");
			String hcdddate = getInfo.getUserInfo(this, "hcdd");
			if("".equalsIgnoreCase(hjddate)||hjddate==null){
				getData("hjd");
			}
			if("".equalsIgnoreCase(gjdqdate)||gjdqdate==null){
				getData("gjdq");
			}
			if("".equalsIgnoreCase(zjzldate)||zjzldate==null){
				getData("zjzl");
			}
			if("".equalsIgnoreCase(hcdddate)||hcdddate==null){
				getData("hcdd");//获取核查地点
			}
			SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("use_name", "cscs");
			editor.putString("use_idcard", "330122199309222818");
			editor.putString("use_dw", "ddwwwdddwww");
			editor.putString("use_phonenum", "13114395517");
			editor.putString("sys_bb", "02");//默认路面版
			editor.putString("abqx", "0");//安保版权限，0无权限，1有权限，默认无权限
			editor.commit();
			getData("abqx");//获取安保权限
		}
	}

	private void getData(String sType){
		HcInfo hcInfo = new HcInfo();
		hcInfo.setSjlx(sType);
		if("abqx".equalsIgnoreCase(sType)){
			hcInfo.setHcr_sfzh(getInfo.getUserInfo(this, "use_idcard"));
		}
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
					}else if(sType.equalsIgnoreCase("abqx")) {
						message.what = 5;
					}else if(sType.equalsIgnoreCase("hcdd")) {
						message.what = 6;
					} else {
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
			switch (msg.what) {
				case 1:  //户籍地
					DmArr = msg.obj.toString();
					//Log.e("darr","1====="+DmArr);
					editor.putString("hjdList", DmArr);
					editor.commit();
					break;
				case 2:  //国籍地区
					DmArr = msg.obj.toString();
					//Log.e("darr","2====="+DmArr);
					editor.putString("gjdqList", DmArr);
					editor.commit();
					break;
				case 3:  //证件类型
					DmArr = msg.obj.toString();
					//Log.e("darr","3====="+DmArr);
					editor.putString("zjzlList", DmArr);
					break;
				case 4:  //数据错误
					Toast.makeText(WelcomeActivity.this, "获取字典项类型错误", Toast.LENGTH_LONG).show();
					editor.putString("hjdList", "");
					editor.putString("gjdqList", "");
					editor.putString("zjzlList", "");
					editor.putString("abqx", "0");
					editor.putString("hcdd", "");
					break;
				case 5:  //安保权限
					DmArr = msg.obj.toString();
					//Log.e("darr","3====="+DmArr);
					editor.putString("abqx", DmArr);
					break;
				case 6:  //核查地点
					DmArr = msg.obj.toString();
					//Log.e("darr","3====="+DmArr);
					editor.putString("hcdd", DmArr);
					break;
				case 0:
					//String result = msg.obj.toString();
					String result = "自动更新字典项失败，请手动获取";
					//Log.e("bbbbuuuuuggggg","====="+msg.obj.toString());
					Toast.makeText(WelcomeActivity.this, result, Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}
			editor.commit();
		}
	};


	//获取测试数据
	private String initData(String sType) {
		String date="";
		String[] names = new String[3];
		String[] sex = new String[2];
		if(sType.equalsIgnoreCase("hjd")){
			names[0] = "地点地点测试111";
			names[1] = "地点地点测试222";
			names[2] = "地点地点测试333";
			sex[0] = "330000";
			sex[1] = "330900";
		}else if(sType.equalsIgnoreCase("zjzl")){
			names[0] = "外交护照";
			names[1] = "公务护照";
			names[2] = "普通护照";
			sex[0] = "11";
			sex[1] = "12";
		}else if(sType.equalsIgnoreCase("gjdq")){
			names[0] = "阿鲁巴";
			names[1] = "阿富汗";
			names[2] = "安哥拉";
			sex[0] = "ABW";
			sex[1] = "AFG";
		}
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject tmpObj = null;

		try {
			for(int i = 0; i < 6000; i++) {
				tmpObj = new JSONObject();
				tmpObj.put("name",names[i%3]);
				tmpObj.put("dm", sex[i%2]);
				jsonArray.put(tmpObj);
				tmpObj = null;
			}
			//String personInfos = jsonArray.toString(); // 将JSONArray转换得到String
			jsonObject.put("res" , jsonArray);   // 获得JSONObject的String
			date = String.valueOf(jsonObject);
		}catch (JSONException e){
			e.printStackTrace();
		}
		return date;
	}

}
