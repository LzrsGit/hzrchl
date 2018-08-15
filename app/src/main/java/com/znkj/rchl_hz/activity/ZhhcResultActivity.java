package com.znkj.rchl_hz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.znkj.rchl_hz.R;
import com.githang.statusbar.StatusBarCompat;
import com.znkj.rchl_hz.utils.DensityUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

public class ZhhcResultActivity extends AppCompatActivity {

    private Button endBtn;
    private TextView title;

    private ImageView resImg;
    private ExpandableTextView bjxx;
    private TextView clbh;
    private TextView cllx;
    private TextView clhm;

    private String res_str;
    private List clresinfo;
    private List ryresinfo;

    private LinearLayout infoLineLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhhc_result_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);
        initView();
        getData();
        endBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(ZhhcResultActivity.this,
                          ZhhcActivity.class);
                  startActivity(intent);
                  ZhhcResultActivity.this.finish();
              }
        });
        if(clresinfo!=null){
            if(!("000".equalsIgnoreCase((String) clresinfo.get(0)))){
                Toast.makeText(ZhhcResultActivity.this, "车辆返回值错误，错误代码："+ clresinfo.get(0), Toast.LENGTH_LONG).show();
                ZhhcResultActivity.this.finish();
            }else {
                setData();
            }
        }

    }

    private void setData(){
        clbh.setText((String)clresinfo.get(2));
        cllx.setText((String)clresinfo.get(3));
        clhm.setText((String)clresinfo.get(4));
        bjxx.setText((String)clresinfo.get(7));
        if(!"".equalsIgnoreCase(((String)clresinfo.get(5)).trim())&&clresinfo.get(5)!=null){
            if("通过".equalsIgnoreCase((String)clresinfo.get(5))){
                resImg.setImageResource(R.drawable.green_cl_icon);
                bjxx.setTextColor(Color.parseColor("#8cc542") );
            }else if("存疑".equalsIgnoreCase((String)clresinfo.get(5))){
                resImg.setImageResource(R.drawable.yellow_cl_icon);
                bjxx.setTextColor(Color.parseColor("#f4b251") );
            }else if("拦截".equalsIgnoreCase((String)clresinfo.get(5))){
                resImg.setImageResource(R.drawable.red_cl_icon);
                bjxx.setTextColor(Color.parseColor("#f25c44") );
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
        infoLineLayout = (LinearLayout)findViewById(R.id.add_ry_info);
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
                JSONArray clArr = null;
                JSONArray ryAllArr = null;
                if(i==0) {
                    try {
                        clArr = (JSONArray) jsArr.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(i==1) {
                    try {
                        ryAllArr = (JSONArray) jsArr.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (clArr!=null) {
                    clresinfo = new ArrayList();
                    for (int j = 0; j <clArr.length(); j++) {
                        try {
                            resArr =  URLDecoder.decode((String)clArr.get(j),"UTF-8");
                            //URLDecoder.decode(obj.getString("updateDesc"), "UTF-8");
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        //Log.e("resArr[" + i + "][" + j + "]===", resArr);
                        clresinfo.add(resArr);
                    }
                }
                if (ryAllArr!=null) {
                    JSONArray ryArr = null;
                    try {
                        for (int k = 0; k <ryAllArr.length(); k++) {
                            Log.e("kkkkkkk","======"+k);
                            String resStr = "";
                            List ryinfo = new ArrayList();
                            ryArr = (JSONArray) ryAllArr.get(k);
                            if(ryArr!=null){
                                for (int m = 0; m <ryArr.length(); m++) {
                                    Log.e("mmmmmmm","======"+m);
                                    try {

                                        resStr =  URLDecoder.decode(ryArr.get(m).toString(),"UTF-8");
                                        Log.e("reStr",m+"================"+resStr);
                                        //URLDecoder.decode(obj.getString("updateDesc"), "UTF-8");
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                    //Log.e("resArr[" + i + "][" + j + "]===", resArr);
                                    ryinfo.add(resStr);
                                }
                                addInfoLinear(infoLineLayout,ryinfo);
                            }
                        }
                    }catch (JSONException e){
                       e.printStackTrace();
                    }
                }
            }
        }
    }

    //添加随车人员
    public void addInfoLinear(LinearLayout linearlayout, List infoList){
        linearlayout.setPadding(DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,0),DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,2));
        addInfoView(linearlayout,infoList);
    }
    private void addInfoView(final LinearLayout lineLayout,final List infoList) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View newLayout = inflater.inflate(R.layout.zhhc_ry_view_item, null);
        ImageView ryResImg = (ImageView)newLayout.findViewById(R.id.res_img);
        ImageView ryHeadImg = (ImageView)newLayout.findViewById(R.id.head_img);
        ExpandableTextView rybjxx = (ExpandableTextView)newLayout.findViewById(R.id.etv);
        TextView hcr = (TextView)newLayout.findViewById(R.id.hcjg_xm);
        TextView hcrxb = (TextView)newLayout.findViewById(R.id.hcjg_xb);
        TextView hcrsfzh = (TextView)newLayout.findViewById(R.id.hcjg_sfzh);
        TextView hcrmz = (TextView)newLayout.findViewById(R.id.hcjg_mz);
        TextView hcrcsrq = (TextView)newLayout.findViewById(R.id.hcjg_csrq);
        TextView hcrhjdz = (TextView)newLayout.findViewById(R.id.hcjg_hjdz);
        if(infoList!=null){
            if(!("000".equalsIgnoreCase((String) infoList.get(0)))){
                Toast.makeText(ZhhcResultActivity.this, "人员返回值错误，错误代码："+ infoList.get(0), Toast.LENGTH_LONG).show();
                ZhhcResultActivity.this.finish();
            }else {
                hcr.setText((String)infoList.get(4));
                hcrxb.setText((String)infoList.get(5));
                hcrsfzh.setText((String)infoList.get(10));
                hcrmz.setText((String)infoList.get(7));
                hcrcsrq.setText((String)infoList.get(6));
                hcrhjdz.setText((String)infoList.get(8));
                if(!"".equalsIgnoreCase((String)infoList.get(11))&&infoList.get(11)!=null&&!"null".equalsIgnoreCase((String)infoList.get(11))){
                    base64ToBitmap((String)infoList.get(11),ryHeadImg);
                }
                rybjxx.setText((String)infoList.get(16));
                if(!"".equalsIgnoreCase(((String)infoList.get(14)).trim())&&infoList.get(14)!=null){
                    if("通过".equalsIgnoreCase((String)infoList.get(14))){
                        ryResImg.setImageResource(R.drawable.green_icon);
                        rybjxx.setTextColor(Color.parseColor("#8cc542") );
                    }else if("存疑".equalsIgnoreCase((String)infoList.get(14))){
                        ryResImg.setImageResource(R.drawable.yellow_icon);
                        rybjxx.setTextColor(Color.parseColor("#f4b251") );
                    }else if("拦截".equalsIgnoreCase((String)infoList.get(14))){
                        ryResImg.setImageResource(R.drawable.red_lj_icon);
                        rybjxx.setTextColor(Color.parseColor("#f25c44") );
                    }else if("抓捕".equalsIgnoreCase((String)infoList.get(14))){
                        ryResImg.setImageResource(R.drawable.red_icon);
                        rybjxx.setTextColor(Color.parseColor("#f25c44") );
                    }
                }
            }
        }
        lineLayout.addView(newLayout);
    }

    private void base64ToBitmap(String base64Data,ImageView view) {
        String get = base64Data.replace(" ", "+");
        byte[] bytes = Base64.decode(get, Base64.DEFAULT);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = 2;
        Bitmap bitmap = null;
        bitmap = (Bitmap) BitmapFactory.decodeByteArray(bytes,0,bytes.length,opts);
        if (bitmap!=null){
            view.setImageBitmap(bitmap);
        }
        if (bytes != null) {
            bytes = null;
        }
    }

}
