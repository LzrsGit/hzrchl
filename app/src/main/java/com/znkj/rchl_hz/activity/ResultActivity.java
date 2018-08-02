package com.znkj.rchl_hz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.znkj.rchl_hz.R;
import com.githang.statusbar.StatusBarCompat;
import com.znkj.rchl_hz.utils.ToBitmapUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

public class ResultActivity extends AppCompatActivity {

    private Button endBtn;
    //private TextView title;
    private ImageView resImg;
    private ImageView headImg;
    private ExpandableTextView bjxx;
    private TextView hcr;
    private TextView hcrxb;
    private TextView hcrsfzh;
    private TextView hcrmz;
    private TextView hcrcsrq;
    private TextView hcrhjdz;

    private String res_str;
    private List resinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);
        getData();
        initView();
        if(resinfo!=null){
            if(!("000".equalsIgnoreCase((String) resinfo.get(0)))){
                Toast.makeText(ResultActivity.this, "返回值错误，错误代码："+ resinfo.get(0), Toast.LENGTH_LONG).show();
                ResultActivity.this.finish();
            }else {
                setData();
            }
        }

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
        headImg = (ImageView)findViewById(R.id.head_img);
        bjxx = (ExpandableTextView)findViewById(R.id.etv);
        hcr = (TextView)findViewById(R.id.hcjg_xm);
        hcrxb = (TextView)findViewById(R.id.hcjg_xb);
        hcrsfzh = (TextView)findViewById(R.id.hcjg_sfzh);
        hcrmz = (TextView)findViewById(R.id.hcjg_mz);
        hcrcsrq = (TextView)findViewById(R.id.hcjg_csrq);
        hcrhjdz = (TextView)findViewById(R.id.hcjg_hjdz);
    }

    private void setData(){
        hcr.setText((String)resinfo.get(4));
        hcrxb.setText((String)resinfo.get(5));
        hcrsfzh.setText((String)resinfo.get(10));
        hcrmz.setText((String)resinfo.get(7));
        hcrcsrq.setText((String)resinfo.get(6));
        hcrhjdz.setText((String)resinfo.get(8));
        if(!"".equalsIgnoreCase((String)resinfo.get(11))&&resinfo.get(11)!=null&&!"null".equalsIgnoreCase((String)resinfo.get(11))){
            base64ToBitmap((String)resinfo.get(11),headImg);
        }
        bjxx.setText((String)resinfo.get(16));
        if(!"".equalsIgnoreCase(((String)resinfo.get(14)).trim())&&resinfo.get(14)!=null){
            if("通过".equalsIgnoreCase((String)resinfo.get(14))){
                resImg.setImageResource(R.drawable.green_icon);
                bjxx.setTextColor(Color.parseColor("#8cc542") );
            }else if("存疑".equalsIgnoreCase((String)resinfo.get(14))){
                resImg.setImageResource(R.drawable.yellow_icon);
                bjxx.setTextColor(Color.parseColor("#f4b251") );
            }else if("拦截".equalsIgnoreCase((String)resinfo.get(14))){
                resImg.setImageResource(R.drawable.red_lj_icon);
                bjxx.setTextColor(Color.parseColor("#f25c44") );
            }else if("抓捕".equalsIgnoreCase((String)resinfo.get(14))){
                resImg.setImageResource(R.drawable.red_icon);
                bjxx.setTextColor(Color.parseColor("#f25c44") );
            }
        }

    }

    private void base64ToBitmap(String base64Data,ImageView view) {
        String get = base64Data.replace(" ", "+");
        byte[] bytes = Base64.decode(get, Base64.DEFAULT);
//        YuvImage yuvimage=new YuvImage(bytes, ImageFormat.NV21, 20,20, null);//20、20分别是图的宽度与高度
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        yuvimage.compressToJpeg(new Rect(0, 0,20, 20), 100, baos);//80--JPG图片的质量[0-100],100最高
//        byte[] jdata = baos.toByteArray();
        BitmapFactory .Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = 2;
//        InputStream input = null;
        Bitmap bitmap = null;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 8;
//        input = new ByteArrayInputStream(jdata);
//
//        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
//                input, null, options));

        bitmap = (Bitmap) BitmapFactory.decodeByteArray(bytes,0,bytes.length,opts);
        if (bitmap!=null){
            view.setImageBitmap(bitmap);
            //bitmap.recycle();
        }

        if (bytes != null) {
            bytes = null;
        }
//        if (jdata != null) {
//            jdata = null;
//        }
//        try {
//            if (input != null) {
//                input.close();
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
       // return bitmap;

        //return BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
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
