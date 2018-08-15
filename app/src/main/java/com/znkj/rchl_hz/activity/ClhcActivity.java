package com.znkj.rchl_hz.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.znkj.rchl_hz.utils.DialogUtils;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.getInfoUtils;
import com.znkj.rchl_hz.widget.MultiSelectPopupWindows;
import com.githang.statusbar.StatusBarCompat;
import com.znkj.rchl_hz.widget.SerachSelectDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClhcActivity extends AppCompatActivity implements View.OnClickListener{

    private Button backBtn;
    private Button addBtn;
    private TextView title;
    private TextView bottom;

    private List<String> cllxDatas;
    private String[] cllxArr = {"",""};
    private String cllxDm="";
    private String cllxMc="";

    private EditText cllx;
    private EditText clhm;
    private EditText scwps;
    private Map<String,String> wpMap = new HashMap<String, String>();

    private LinearLayout linearLayoutProductType;
    private List<Search> products;
    private MultiSelectPopupWindows productsMultiSelectPopupWindows;
    private getInfoUtils getInfo;
    private LinearLayout lineLayout;
    private Dialog mDialog;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:  //访问成功，有数据
                    Intent intent = new Intent(ClhcActivity.this,
                            ClhcResultActivity.class);
                    intent.putExtra("result", msg.obj.toString());
                    startActivity(intent);
                    finish();
                    break;
                case 0:
                    //String result = msg.obj.toString();
                    String result = "提交失败！";
                    Toast.makeText(ClhcActivity.this, result, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            DialogUtils.closeDialog(mDialog);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.clhc_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);

        linearLayoutProductType = (LinearLayout) findViewById(R.id.wjwp_list);
        linearLayoutProductType.setOnClickListener(this);
        getData();
        initView();
        initData();
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
                SubmitInfo();
//                Intent intent = new Intent(ClhcActivity.this,
//                        ClhcResultActivity.class);
//                startActivity(intent);

//                for (Map.Entry<String, String> entry : wpMap.entrySet()) {
//                    //System.out.println(entry.getKey() + " ：" + entry.getValue());
//                    Log.e("mapkkkkk","==="+entry.getKey());
//                    Log.e("mapvvvvv","==="+entry.getValue());
//                }
            }
        });
        cllx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSelectDialog(cllx);
            }
        });

    }

    public void initView(){
        title = (TextView)findViewById(R.id.text_title);
        title.setText("车辆核查");
        bottom = (TextView)findViewById(R.id.bottom_address);
        bottom.setText(getInfo.getAddress(this));
        cllx = (EditText)findViewById(R.id.clhc_cllx);
        cllx.setInputType(InputType.TYPE_NULL);//不弹出键盘
        clhm = (EditText)findViewById(R.id.clhc_cph);
        scwps = (EditText)findViewById(R.id.clhc_scwps);
    }

    private void initData() {
        cllxDatas = new ArrayList<>();
        String[] cllxs = {"01:小型车", "02:大型车", "03:摩托车", "04:其他"};
        for (int j = 0; j < cllxs.length; j++) {
            cllxDatas.add(cllxs[j]);
        }

    }

    //弹框选择方法
    public void openSelectDialog(EditText eText) {
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(this);
        alert.setListData(cllxDatas);
        //alert.setTitle("请选择名族");
        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(info!=null&&!("".equalsIgnoreCase(info.trim()))){
                    cllxArr = info.split(":");
                    cllxDm = cllxArr[0];
                    cllxMc = cllxArr[1];
                }
                eText.setText(cllxMc);
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9, 0.6, this);
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
                        addLinear(res_list.get(j).getKeyWord(),res_list.get(j).getNo());
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
        products.add(new Search("被盗抢机动车（辆）", false, "01"));
        products.add(new Search("冒用、套用车牌（套）", false, "02"));
        products.add(new Search("枪支（支）", false, "03"));
        products.add(new Search("仿真枪（支））", false, "04"));
        products.add(new Search("子弹（发）", false, "05"));
        products.add(new Search("炸药（千克）", false, "06"));
        products.add(new Search("雷管（枚）", false, "07"));
        products.add(new Search("烟花爆竹（头）", false, "08"));
        products.add(new Search("危险化学品（千克）", false, "09"));
        products.add(new Search("毒品（克）", false, "10"));
        products.add(new Search("管制刀具（把）", false, "11"));
        products.add(new Search("非法出版物及音像品（件）", false, "12"));
        products.add(new Search("低慢小目标（个）", false, "13"));
        products.add(new Search("其他物品（件）", false, "99"));
    }


    //添加增加按钮
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addLinear(String name,String code){
        lineLayout = findViewById(R.id.add_linear);
        lineLayout.setPadding(DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5));
        addView(lineLayout,name,code);
    }
    private void removeLinear(){
        lineLayout = findViewById(R.id.add_linear);
        lineLayout.setPadding(0,0,0,0);
        lineLayout.removeAllViews();
        wpMap.clear();//清空map
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addView(final LinearLayout lineLayout,String name,String code) {
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
        //showText.setTag();
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
        vlaueText.setTag(code);// 设置tag
        wpMap.put(code,"1");// 默认数量为1
        vlaueText.setInputType(InputType.TYPE_CLASS_NUMBER);
        vlaueText.setText("1");// 默认数量为1
        //设置可输入的最大值
        vlaueText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        vlaueText.setBackgroundColor(Color.WHITE);
        vlaueText.setGravity(Gravity.CENTER);
        vlaueText.setHeight(DensityUtil.dip2px(this,40));
        vlaueText.setWidth(DensityUtil.dip2px(this,40));
        vlaueText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //输入内容之前你想做什么
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入的时候你想做什么
            }
            public void afterTextChanged(Editable s) {
                //输入之后你想做什么
                wpMap.put((String)vlaueText.getTag(),s.toString());// 默认数量为1
            }
        });
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
                    vlaueText.setText("1");
                }else {
                    if (1 != Integer.valueOf(vlaueText.getText().toString())) {
                        vlaueText.setText((Integer.valueOf(vlaueText.getText().toString()) - 1) + "");
                    } else {
                        Toast.makeText(ClhcActivity.this, "该值不能小于1", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if ("".equalsIgnoreCase(vlaueText.getText().toString())||null==vlaueText.getText().toString()) {
                    vlaueText.setText("1");
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

    public void SubmitInfo() {
        boolean wjwps =true;
        if (cllxDm.trim().equalsIgnoreCase("")
                || clhm.getText().toString().trim().equalsIgnoreCase("")
            //                || scwps.getText().toString().trim().equalsIgnoreCase("")
                ) {
            Toast.makeText(ClhcActivity.this, "您输入的信息不完整！",
                    Toast.LENGTH_SHORT).show();
        } else {
            if(wpMap.size()>0){
                //Log.e("scwps","============"+scwps.getText().toString());
                if(!(scwps.getText().toString().trim().equalsIgnoreCase(""))
                        &&scwps.getText().toString()!=null){
                    if(Integer.valueOf(scwps.getText().toString())<wpMap.size()){
                        wjwps = false;
                        Toast.makeText(ClhcActivity.this, "随车物品数不能小于已选择的违禁物品数！",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        wjwps = true;
                    }
                }else {
                    wjwps = false;
                    Toast.makeText(ClhcActivity.this, "随车物品数不能小于已选择的违禁物品数！",
                            Toast.LENGTH_SHORT).show();
                }
            }
            if(wjwps){
                mDialog = DialogUtils.createLoadingDialog(ClhcActivity.this,"提交中...");
                try {
                    HcInfo hcInfo = new HcInfo();
                    hcInfo.setSjlx("hc");
                    hcInfo.setHclx("cl");
                    hcInfo.setHcbb(getInfo.getUserInfo(this,"sys_bb"));
                    hcInfo.setHcr_xm(getInfo.getUserInfo(this,"use_name"));
                    hcInfo.setHcr_sjh(getInfo.getUserInfo(this,"use_phonenum"));
                    hcInfo.setHcr_sfzh(getInfo.getUserInfo(this,"use_idcard"));
                    hcInfo.setHcdz(getInfo.getAddress(this));
                    hcInfo.setCl_cllx(cllxDm.trim());
                    hcInfo.setCl_clhm(clhm.getText().toString().trim().toUpperCase());
                    hcInfo.setCl_scwps(scwps.getText().toString().trim());
                    if(wpMap.size()>0){
                        JSONObject jsonObject = new JSONObject();
                        for (Map.Entry<String, String> entry : wpMap.entrySet()) {
                            //System.out.println(entry.getKey() + " ：" + entry.getValue());
                            //Log.e("mapkkkkk","==="+entry.getKey());
                            //Log.e("mapvvvvv","==="+entry.getValue());
                            jsonObject.put(entry.getKey(),entry.getValue());
                        }
                        hcInfo.setCl_wjwp(jsonObject);
                        Log.e("hhhcccc1","==="+ hcInfo.getCl_wjwp());
                        Log.e("hhhcccc2","==="+ hcInfo.getCl_wjwp().toString().trim());
                        hcInfo.setYwwjwp("1");
                    }else {
                        //hcInfo.setCl_wjwp("wu");
                        hcInfo.setYwwjwp("0");
                    }
                    //String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
                    HttpUtil.sendHcRequest(this.getString(R.string.url_address), hcInfo, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Message message = new Message();
                            if("WRONG".equalsIgnoreCase(response.trim())){
                                message.what = 0;
                            }else {
                                message.what = 1;
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
