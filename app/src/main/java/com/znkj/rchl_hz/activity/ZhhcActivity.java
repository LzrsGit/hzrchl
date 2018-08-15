package com.znkj.rchl_hz.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.znkj.rchl_hz.utils.AddViewUtils;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ZhhcActivity extends AppCompatActivity implements View.OnClickListener{

    private Button backBtn;
    private Button addBtn;
    private Button hcBtn;
    private TextView title;
    private TextView bottom;
   // private TextView peo_num;

    private List<String> cllxDatas;
    private String[] cllxArr = {"",""};
    private String cllxDm="";
    private String cllxMc="";

    private EditText cllx;
    private EditText clhm;
    private EditText scwps;

    private LinearLayout linearLayoutProductType;
    private List<Search> products;
    private MultiSelectPopupWindows productsMultiSelectPopupWindows;
    private getInfoUtils getInfo;
    private LinearLayout lineLayout;
    private LinearLayout infoLineLayout;

    private AddViewUtils addView = new AddViewUtils();
    private Map<String,String> wpMap = new HashMap<String, String>();
    private ImageView sj_btn;
    private ImageView del_btn;
    private int id_num = 1000;
    private List<ImageView> sj_btn_list = new ArrayList<ImageView>();
    private List<View> info_view_list = new ArrayList<View>();
    private Map<String,String> sfsjMap = new HashMap<String, String>();
    private Drawable sj_f;
    private Drawable sj_t;
    private String zh_ryxx = "";
    private EditText djsfzhTex;

    private Dialog mDialog;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:  //访问成功，有数据
                    Intent intent = new Intent(ZhhcActivity.this,
                            ZhhcResultActivity.class);
                    intent.putExtra("result", msg.obj.toString());
                    startActivity(intent);
                    finish();
                    break;
                case 0:
                    //String result = msg.obj.toString();
                    String result = "提交失败！";
                    Toast.makeText(ZhhcActivity.this, result, Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.zhhc_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);

        lineLayout = (LinearLayout)findViewById(R.id.add_linear);
        infoLineLayout = (LinearLayout)findViewById(R.id.add_peo_inf);
        linearLayoutProductType = (LinearLayout) findViewById(R.id.wjwp_list);
        linearLayoutProductType.setOnClickListener(this);
        register();
        getData();
        initView();
        initData();
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  ZhhcActivity.this.finish();
              }
        });
        hcBtn = findViewById(R.id.zhhc_hc_btn);
        hcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitInfo();
            }
        });
        addBtn = (Button)findViewById(R.id.zhhc_add_btn);
        //peo_num = (TextView)findViewById(R.id.peo_num);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //peonum++;
                //peo_num.setText("当前人数："+peonum);
               addInfoLinear(infoLineLayout);
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
        title.setText("综合核查");
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
                for (int i=0; i<products.size();i++){
                    temp = products.get(i);
                    if (temp.isChecked()){
                        res_list.add(temp);
                    }
                }
                if (res_list!=null){
                    addView.removeLinear(lineLayout);
                    wpMap.clear();
                    for (int j=0; j<res_list.size(); j++){
                        //str+=res_list.get(j).getKeyWord();
                        wpMap = addView.addLinear(res_list.get(j).getKeyWord(),res_list.get(j).getNo(),lineLayout,ZhhcActivity.this);
                    }
                }else {
                    addView.removeLinear(lineLayout);
                    wpMap.clear();
                }

                //Toast.makeText(ZhhcActivity.this, "我消失了，你可以做点什么。======"+str, Toast.LENGTH_SHORT).show();
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

    public void SubmitInfo() {
        boolean wjwps =true;//违禁物品判断
        boolean rysfzh =true;//人员身份信息填写判断
        boolean sjxx =false;//司机选择判断
        View tempV;
        View cV;
        EditText tempSjhm;
        EditText tempSfzh;
        String[] vTagArr={"",""};
        String vTagNum;
        String sfsjVal;
        if (cllxDm.trim().equalsIgnoreCase("")
                || clhm.getText().toString().trim().equalsIgnoreCase("")
            //                || scwps.getText().toString().trim().equalsIgnoreCase("")
                ) {
            Toast.makeText(ZhhcActivity.this, "您输入的信息不完整！",
                    Toast.LENGTH_SHORT).show();
        } else {
            if(wpMap.size()>0){
                if(!scwps.getText().toString().trim().equalsIgnoreCase("")
                        &&scwps.getText().toString()!=null){
                    if(Integer.valueOf(scwps.getText().toString())<wpMap.size()){
                        wjwps = false;
                        Toast.makeText(ZhhcActivity.this, "随车物品数不能小于已选择的违禁物品数！",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        wjwps = true;
                    }
                }else {
                    wjwps = false;
                    Toast.makeText(ZhhcActivity.this, "随车物品数不能小于已选择的违禁物品数！",
                            Toast.LENGTH_SHORT).show();
                }
            }
            if(info_view_list.size()>0){
                Log.e("viewlistSize","====="+info_view_list.size());
                for (int vv=0; vv<info_view_list.size();vv++){
                    tempV = info_view_list.get(vv);
                    cV = infoLineLayout.findViewWithTag(tempV.getTag());
                    if(cV!=null){
                        vTagArr = ((String)tempV.getTag()).split(":");
                        vTagNum = vTagArr[1];
                        tempSjhm = (EditText)tempV.findViewWithTag("sjhm:"+vTagNum);
                        tempSfzh = (EditText)tempV.findViewWithTag("sfzh:"+vTagNum);
                        sfsjVal = sfsjMap.get("sj:"+vTagNum);
                        String ryinfo = "sfzh:sjh:sfsj";
                        ryinfo = tempSfzh.getText().toString()+":"
                                +tempSjhm.getText().toString()+":"+
                                sfsjVal;
                        if(zh_ryxx.equalsIgnoreCase("")){
                            zh_ryxx +=ryinfo;
                        }else {
                            zh_ryxx +=","+ryinfo;
                        }
                        Log.e("arrrzhryxx","===="+zh_ryxx);
                        if(tempSfzh.getText().toString()==null
                                ||tempSfzh.getText().toString().equalsIgnoreCase("")){
                            rysfzh = false;
                        }
                        if(sfsjVal!=null&&!("".equalsIgnoreCase(sfsjVal))){
                            if("1".equalsIgnoreCase(sfsjVal)){
                                sjxx = true;
                            }
                        }

                    }
                }
                if(!sjxx){
                    Toast.makeText(ZhhcActivity.this, "请选择一个司机！",
                            Toast.LENGTH_SHORT).show();
                    zh_ryxx = "";//清空人员信息
                }
                if(!rysfzh){
                    Toast.makeText(ZhhcActivity.this, "一个或多个人员身份证号未填写！",
                            Toast.LENGTH_SHORT).show();
                    zh_ryxx = "";//清空人员信息
                }
            }
            if(wjwps&&rysfzh&&sjxx){
                mDialog = DialogUtils.createLoadingDialog(ZhhcActivity.this,"提交中...");
                try {
                    HcInfo hcInfo = new HcInfo();
                    hcInfo.setSjlx("hc");
                    hcInfo.setHclx("zh");
                    hcInfo.setHcbb(getInfo.getUserInfo(this,"sys_bb"));
                    hcInfo.setHcr_xm(getInfo.getUserInfo(this,"use_name"));
                    hcInfo.setHcr_sjh(getInfo.getUserInfo(this,"use_phonenum"));
                    hcInfo.setHcr_sfzh(getInfo.getUserInfo(this,"use_idcard"));
                    hcInfo.setHcdz(getInfo.getAddress(this));
                    hcInfo.setCl_cllx(cllxDm.trim());
                    hcInfo.setCl_clhm(clhm.getText().toString().trim());
                    hcInfo.setCl_scwps(scwps.getText().toString().trim());
                    hcInfo.setZh_ryxx(zh_ryxx.trim().toUpperCase());
                    Log.e("zhryxx","===="+zh_ryxx);
                    zh_ryxx = "";
                    if(wpMap.size()>0){
                        JSONObject jsonObject = new JSONObject();
                        for (Map.Entry<String, String> entry : wpMap.entrySet()) {
                            //System.out.println(entry.getKey() + " ：" + entry.getValue());
                            //Log.e("mapkkkkk","==="+entry.getKey());
                            //Log.e("mapvvvvv","==="+entry.getValue());
                            jsonObject.put(entry.getKey(),entry.getValue());
                        }
                        hcInfo.setCl_wjwp(jsonObject);
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


    //添加随车人员
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addInfoLinear(LinearLayout linearlayout){
        linearlayout.setPadding(DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5),DensityUtil.dip2px(this,5));
        addInfoView(linearlayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addInfoView(final LinearLayout lineLayout) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View newLayout = inflater.inflate(R.layout.add_view_item, null);
        EditText sjh = (EditText) newLayout.findViewById(R.id.sjhm);
        EditText sfzh = (EditText) newLayout.findViewById(R.id.sfzh);
        TextView dj_sfzh = (TextView) newLayout.findViewById(R.id.dj_sfzh);
        //sfsj = (EditText) newLayout.findViewById(R.id.sfsj);
        if(id_num==20000){
            id_num = 0;
        }
        newLayout.setTag("layout:"+id_num);
        sjh.setTag("sjhm:"+id_num);
        sfzh.setTag("sfzh:"+id_num);
        dj_sfzh.setTag("dj_sfzh:"+id_num);
        info_view_list.add(newLayout);
        sj_f = this.getDrawable(R.drawable.sj_f);
        sj_t = this.getDrawable(R.drawable.sj_t);
        sj_btn = (ImageView)newLayout.findViewById(R.id.sj);
        del_btn = (ImageView)newLayout.findViewById(R.id.del_btn);
        sj_btn.setTag("sj:"+id_num);
        del_btn.setTag(id_num);
        del_btn.setOnClickListener(vClick);
        dj_sfzh.setOnClickListener(djClick);
        sj_btn_list.add(sj_btn);
        sfsjMap.put((String)sj_btn.getTag(),"0");//默认不是司机
        id_num++;
        sj_btn.setOnClickListener(pChildClick);
        //分割线
        final LinearLayout layout_line=new LinearLayout(this);
        LinearLayout.LayoutParams params_line = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dip2px(this,10));
        //params_line.setMargins(DensityUtil.dip2px(context,105),0,DensityUtil.dip2px(context,20),0);
        //layout_line.setBackgroundColor(Color.parseColor("#D7D7D7"));
        layout_line.setOrientation(LinearLayout.VERTICAL);
        layout_line.setLayoutParams(params_line);
        final LinearLayout layout_ad=new LinearLayout(this);
        LinearLayout.LayoutParams params_ad = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_ad.setOrientation(LinearLayout.VERTICAL);
        layout_ad.setLayoutParams(params_ad);
        layout_ad.addView(newLayout);
        layout_ad.addView(layout_line);
        //加入布局
        lineLayout.addView(layout_ad);
    }

    View.OnClickListener pChildClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            final Object tag = v.getTag();
            if (tag!=null){
                ImageView temp_imgv = (ImageView) v.findViewWithTag(tag);
                temp_imgv.setImageDrawable(sj_t);
                sfsjMap.put((String)tag,"1");
                for (int ii=0;ii<sj_btn_list.size();ii++){
                    String iitag = (String)sj_btn_list.get(ii).getTag();
                    if(!iitag.equalsIgnoreCase((String)tag)){
                        ImageView temp_imgv1 = sj_btn_list.get(ii);
                        temp_imgv1.setImageDrawable(sj_f);
                    }
                }
                for (Map.Entry<String, String> entry : sfsjMap.entrySet()) {
                    //System.out.println(entry.getKey() + " ：" + entry.getValue());
                    if(!(entry.getKey().equalsIgnoreCase((String)tag))){
                        sfsjMap.put(entry.getKey(),"0");
                    }
                    //Log.e("mapkkkkk","==="+entry.getKey());
                    //Log.e("mapvvvvv","==="+entry.getValue());
                }

            }
        }
    };
    View.OnClickListener vClick = new View.OnClickListener(){
        public void onClick(View v) {
            final Object tag = v.getTag();
            if (tag!=null){
                ImageView temp_imgv = v.findViewWithTag(tag);
                ViewGroup view = (ViewGroup)temp_imgv.getParent();
                if(null!=view){
                    ViewGroup view_pa = (ViewGroup)view.getParent();
                    ViewGroup view_pa_p = (ViewGroup)view_pa.getParent();
                    view_pa_p.removeView(view_pa);
                }

                //删除司机信息
                Iterator<String> iterator = sfsjMap.keySet().iterator();// map中key（键）的迭代器对象
                while (iterator.hasNext()){// 循环取键值进行判断
                    String key = iterator.next();// 键
                    Log.e("sckey","===="+key);
                    Log.e("sctag","===="+tag.toString());
                    if(key.equalsIgnoreCase("sj:"+tag.toString())){
                        iterator.remove();// 移除map中对应的键值对
                    }
                }

            }
        }
    };

    //nfc读卡点击
    View.OnClickListener djClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            final Object tag = v.getTag();
            if (tag!=null){
                String ediTag = "";
                ediTag = "sfzh:"+((String)tag).substring(((String)tag).indexOf(":")+1);
                //Log.e("ttttttaaaagggg","===="+tag);
                //Log.e("eeettttttaaaagggg","===="+ediTag);
                TextView temp_texv = v.findViewWithTag(tag);
                ViewGroup view = (ViewGroup)temp_texv.getParent();
                djsfzhTex = (EditText) view.findViewWithTag(ediTag);//绑定获取身份证号输入框
                //开启读卡插件
                Intent intent = new Intent("cybertech.pstore.intent.action.NFC_READER");
                intent.setPackage("cn.com.cybertech.nfc.reader");
                startActivity(intent);
            }else {
                Toast.makeText(ZhhcActivity.this, "开启插件失败，请手动输入身份证号", Toast.LENGTH_LONG).show();
            }


            }
    };
    //nfc读卡
    /*通过NFC读取身份证信息成功*/
    public static final String ACTION_NFC_READ_IDCARD_SUCCESS = "cybertech.pstore.intent.action.NFC_READ_IDCARD_SUCCESS";
    /*通过NFC读取身份证信息失败*/
    public static final String ACTION_NFC_READ_IDCARD_FAILURE = "cybertech.pstore.intent.action.NFC_READ_IDCARD_FAILURE";
    public void onDestroy() {
        super.onDestroy();
        unregister();
    }
    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NFC_READ_IDCARD_SUCCESS);
        filter.addAction(ACTION_NFC_READ_IDCARD_FAILURE);
        this.registerReceiver(mReceiver, filter);
    }
    private void unregister() {
        this.unregisterReceiver(mReceiver);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle data = intent.getExtras();
            if (ACTION_NFC_READ_IDCARD_SUCCESS.equals(action)) {
                // TODO: 获取身份证数据成功
                showContent2(data);
            } else if (ACTION_NFC_READ_IDCARD_FAILURE.equals(action)) {
                // TODO: 获取身份证数据失败
                Toast.makeText(ZhhcActivity.this, "读取身份信息失败，请手动输入身份证号", Toast.LENGTH_LONG).show();
            }
        }
    };
    protected void showContent2(Bundle data) {
        if (data != null) {
            StringBuilder sb = new StringBuilder();
            for (String key : data.keySet()) {
                if(key.equalsIgnoreCase("identity")){
                    sb.append(data.get(key));
                }
            }
            djsfzhTex.setText(sb.toString());
        }
    }

}
