package com.znkj.rchl_hz.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.model.Search;
import com.znkj.rchl_hz.utils.AddViewUtils;
import com.znkj.rchl_hz.utils.getInfoUtils;
import com.znkj.rchl_hz.widget.MultiSelectPopupWindows;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

public class ZhhcActivity extends AppCompatActivity implements View.OnClickListener{

    private Button backBtn;
    private Button addBtn;
    private Button hcBtn;
    private TextView title;
    private TextView bottom;
    private TextView peo_num;

    private LinearLayout linearLayoutProductType;
    private List<Search> products;
    private MultiSelectPopupWindows productsMultiSelectPopupWindows;
    private getInfoUtils getInfo;
    private LinearLayout lineLayout;
    private LinearLayout infoLineLayout;

    private AddViewUtils addView = new AddViewUtils();
    private int peonum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhhc_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);

        lineLayout = (LinearLayout)findViewById(R.id.add_linear);
        infoLineLayout = (LinearLayout)findViewById(R.id.add_peo_inf);
        linearLayoutProductType = (LinearLayout) findViewById(R.id.wjwp_list);
        linearLayoutProductType.setOnClickListener(this);
        getData();
        initView();
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
                Intent intent = new Intent(ZhhcActivity.this,
                        ZhhcResultActivity.class);
                startActivity(intent);
            }
        });
        addBtn = (Button)findViewById(R.id.zhhc_add_btn);
        peo_num = (TextView)findViewById(R.id.peo_num);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                peonum++;
                peo_num.setText("当前人数："+peonum);
                addView.addInfoLinear(ZhhcActivity.this,infoLineLayout);
            }
        });

    }

    public void initView(){
        title = (TextView)findViewById(R.id.text_title);
        title.setText("综合核查");
        bottom = (TextView)findViewById(R.id.bottom_address);
        bottom.setText(getInfo.getAddress(this));
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
                    for (int j=0; j<res_list.size(); j++){
                        //str+=res_list.get(j).getKeyWord();
                        addView.addLinear(res_list.get(j).getKeyWord(),lineLayout,ZhhcActivity.this);
                    }
                }else {
                    addView.removeLinear(lineLayout);
                }

                //Toast.makeText(ZhhcActivity.this, "我消失了，你可以做点什么。======"+str, Toast.LENGTH_SHORT).show();
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

}
