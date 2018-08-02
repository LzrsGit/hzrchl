package com.znkj.rchl_hz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.WelcomeActivity;
import com.znkj.rchl_hz.model.HcInfo;

import java.util.Map;

public class getInfoUtils {

    //private String DmArr="";

    public static String getAddress(Context context){
        String Add = "";
        SharedPreferences sp = context.getSharedPreferences("address", Context.MODE_PRIVATE);
        Add = sp.getString("add", "");
        return Add;
    }

    public static String getUserInfo(Context context, String key){
        String info = "";
        SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        info = sp.getString(key, "");
        return info;
    }

    public static void delUseInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("use_name", "");
        editor.putString("use_idcard", "");
        editor.putString("use_id", "");
        editor.putString("use_phonenum", "");
        editor.putString("sys_bb", "02");
        editor.commit();
    }

    public String[] getLocal(Context context,String[] localArr){
        Map<String, String> locationInfo = null;
        try {
            locationInfo = cn.com.cybertech.pdk.LocationInfo.getLocationInfo(context);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(locationInfo!=null){
            if(localArr.length!=2){
                Toast.makeText(context, "localArr error", Toast.LENGTH_SHORT).show();
                String[] errorArr = {"",""};
                return errorArr;
            }else {
                localArr[0] = locationInfo.get("longitude");//longitude
                localArr[1] = locationInfo.get("latitude");//latitude
            }
        }
        return localArr;
    }

//    private String getData(String sType,Context context){
//        final String data = "";
//        HcInfo hcInfo = new HcInfo();
//        hcInfo.setSjlx(sType);
//        HttpUtil.sendHcRequest(context.getString(R.string.url_address), hcInfo, new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) {
//                if ("WRONG".equalsIgnoreCase(response.trim())) {
//                    String result = "自动更新字典项失败，请手动获取";
//                    //Log.e("bbbbuuuuuggggg","====="+msg.obj.toString());
//                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
//                } else {
//                    data = response.toString();
//                }
//            }
//            @Override
//            public void onError(Exception e) {
//                String result = "自动更新字典项失败，请手动获取";
//                //Log.e("bbbbuuuuuggggg","====="+msg.obj.toString());
//                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
//            }
//        });
//        return data;
//    }

}
