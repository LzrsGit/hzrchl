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
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.model.Search;
import com.znkj.rchl_hz.utils.DensityUtil;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.getInfoUtils;
import com.znkj.rchl_hz.widget.MultiSelectPopupWindows;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

public class ClhcActivity extends AppCompatActivity implements View.OnClickListener{

    private Button backBtn;
    private Button addBtn;
    private TextView title;
    private TextView bottom;

    private EditText cllx;
    private EditText clhm;
    private EditText scwps;

    private LinearLayout linearLayoutProductType;
    private List<Search> products;
    private MultiSelectPopupWindows productsMultiSelectPopupWindows;
    private getInfoUtils getInfo;
    private LinearLayout lineLayout;

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
            Toast.makeText(ClhcActivity.this, result, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clhc_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);

        linearLayoutProductType = (LinearLayout) findViewById(R.id.wjwp_list);
        linearLayoutProductType.setOnClickListener(this);
        getData();
        initView();
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  ClhcActivity.this.finish();
              }
        });
        addBtn = findViewById(R.id.clhc_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //暂时屏蔽
                //SubmitInfo();
                Intent intent = new Intent(ClhcActivity.this,
                        ClhcResultActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initView(){
        title = (TextView)findViewById(R.id.text_title);
        title.setText("车辆核查");
        bottom = (TextView)findViewById(R.id.bottom_address);
        bottom.setText(getInfo.getAddress(this));
        cllx = (EditText)findViewById(R.id.clhc_cllx);
        clhm = (EditText)findViewById(R.id.clhc_cph);
        scwps = (EditText)findViewById(R.id.clhc_scwps);
    }

    public void SubmitInfo() {

        if (cllx.getText().toString().trim().equalsIgnoreCase("")
                || clhm.getText().toString().trim().equalsIgnoreCase("")
                || scwps.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(ClhcActivity.this, "您输入的信息不完整！",
                    Toast.LENGTH_SHORT).show();
        } else {

            try {
                HcInfo hcInfo = new HcInfo();
                hcInfo.setHclx("cl");
                hcInfo.setHcbb(getInfo.getUserInfo(this,"sys_bb"));
                hcInfo.setHcr_xm(getInfo.getUserInfo(this,"use_name"));
                hcInfo.setHcr_sjh(getInfo.getUserInfo(this,"use_phonenum"));
                hcInfo.setHcr_sfzh(getInfo.getUserInfo(this,"use_idcard"));
                hcInfo.setHcdz(getInfo.getAddress(this));
                hcInfo.setCl_cllx(cllx.getText().toString().trim());
                hcInfo.setCl_clhm(clhm.getText().toString().trim());
                hcInfo.setCl_scwps(scwps.getText().toString().trim());
                //String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
                HttpUtil.sendHcRequest(this.getString(R.string.url_address), hcInfo, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Message message = new Message();
                        message.obj = response;
                        mHandler.sendMessage(message);
                        //Log.i("sssssss=====",user.getUserName());
                        //Intent intent = new Intent(AddStudentActivity.this,
                        //		AddStudentActivity.class);
                        //StudentInformationManagerActivity.class);
                        //intent.putExtra("xm", jbr.getText().toString());
                        //intent.putExtra("sjh",jbr_phone.getText().toString());
                        //intent.putExtra("sfzh", jbr_id.getText().toString());
                        //startActivity(intent);
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

    @Override
    public void onClick(View v) {
        productsMultiSelectPopupWindows = new MultiSelectPopupWindows(this, linearLayoutProductType, 240, products);
        productsMultiSelectPopupWindows.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDismiss() {
                Search temp = new Search("选择",false,"222");
                List<Search> res_list =  new ArrayList<>();
                String str = "";
                for (int i=0; i<products.size();i++){
                    temp = products.get(i);
                    if (temp.isChecked()){
                        res_list.add(temp);
                    }
                }
                if (res_list!=null){
                    removeLinear();
                    for (int j=0; j<res_list.size(); j++){
                        //res_list添加数量后可以传给后台
                        str+=res_list.get(j).getKeyWord();
                        addLinear(res_list.get(j).getKeyWord());
                    }
                }else {
                    removeLinear();
                }

                //Toast.makeText(ClhcActivity.this, "我消失了，你可以做点什么。======"+str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        products = new ArrayList<>();
        products.add(new Search("被盗抢机动车（辆）", false, "0"));
        products.add(new Search("枪支（支）", false, "1"));
        products.add(new Search("子弹（发）", false, "2"));
        products.add(new Search("炸药（千克）", false, "3"));
        products.add(new Search("雷管（枚）", false, "4"));
        products.add(new Search("毒品（克）", false, "5"));
        products.add(new Search("管制刀具（把）", false, "6"));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addLinear(String name){
        lineLayout = findViewById(R.id.add_linear);
        lineLayout.setPadding(DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5));
        addView(lineLayout,name);
    }
    private void removeLinear(){
        lineLayout = findViewById(R.id.add_linear);
        lineLayout.setPadding(0,0,0,0);
        lineLayout.removeAllViews();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addView(final LinearLayout lineLayout,String name) {
        //文本lauout
        final LinearLayout layout2=new LinearLayout(this);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setBackgroundColor(Color.WHITE);
        layout2.setPadding(DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5));
        LinearLayout.LayoutParams params_layout2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params_layout2.setMargins(DensityUtil.dip2px(this,90),0,DensityUtil.dip2px(this,20),0);
        layout2.setLayoutParams(params_layout2);
        //分割线layout
        final LinearLayout layout_line=new LinearLayout(this);
        LinearLayout.LayoutParams params_line = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dip2px(this,1));
        params_line.setMargins(DensityUtil.dip2px(this,105),0,DensityUtil.dip2px(this,20),0);
        layout_line.setBackgroundColor(Color.parseColor("#D7D7D7"));
        layout_line.setOrientation(LinearLayout.VERTICAL);
        layout_line.setLayoutParams(params_line);

        //text
        final TextView showText = new TextView(this);
        showText.setTextColor(Color.GRAY);
        showText.setTextSize(14);
        //showText.setId(10001);//设置 id
        showText.setText(name);
        showText.setBackgroundColor(Color.WHITE);
        showText.setGravity(Gravity.CENTER_VERTICAL);
        showText.setHeight(DensityUtil.dip2px(this,40));
        showText.setWidth(DensityUtil.dip2px(this,140));

        //value_text
        final EditText vlaueText = new EditText(this);
        vlaueText.setTextColor(Color.GRAY);
        vlaueText.setTextSize(14);
        //vlaueText.setId(10001);//设置 id
        vlaueText.setInputType(InputType.TYPE_CLASS_NUMBER);
        vlaueText.setText("0");
        //设置可输入的最大值
        vlaueText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        vlaueText.setBackgroundColor(Color.WHITE);
        vlaueText.setGravity(Gravity.CENTER);
        vlaueText.setHeight(DensityUtil.dip2px(this,40));
        vlaueText.setWidth(DensityUtil.dip2px(this,40));

        //创建按钮
        ImageView btn_del = new ImageView(this);
        ImageView btn_add = new ImageView(this);
        Drawable del = getDrawable(R.drawable.del);
        Drawable add = getDrawable(R.drawable.add);
        btn_del.setImageDrawable(del);
        btn_add.setImageDrawable(add);
        LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this,20),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_params.gravity = Gravity.CENTER_HORIZONTAL;
        btn_del.setLayoutParams(btn_params);
        btn_add.setLayoutParams(btn_params);

        //点击按钮
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if ("".equalsIgnoreCase(vlaueText.getText().toString())||null==vlaueText.getText().toString()) {
                    vlaueText.setText("0");
                }else {
                    if (0 != Integer.valueOf(vlaueText.getText().toString())) {
                        vlaueText.setText((Integer.valueOf(vlaueText.getText().toString()) - 1) + "");
                    } else {
                        Toast.makeText(ClhcActivity.this, "该值不能小于0", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if ("".equalsIgnoreCase(vlaueText.getText().toString())||null==vlaueText.getText().toString()) {
                    vlaueText.setText("0");
                }else {
                    if (999 != Integer.valueOf(vlaueText.getText().toString())) {
                        vlaueText.setText((Integer.valueOf(vlaueText.getText().toString()) + 1) + "");
                    } else {
                        Toast.makeText(ClhcActivity.this, "该值不能大于999", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //添加文本到布局
        layout2.addView(showText );
        layout2.addView(btn_del);
        layout2.addView(vlaueText);
        layout2.addView(btn_add);
        // 动态添加按钮到主布局
        lineLayout.addView(layout2);
        lineLayout.addView(layout_line);

    }
}
