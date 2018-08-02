package com.znkj.rchl_hz.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.adapter.BaseRecyclerAdapter;
import com.znkj.rchl_hz.adapter.FuzzMatchPersonAdapter;
import com.znkj.rchl_hz.model.PersonBean;
import com.znkj.rchl_hz.model.fragmentTag;
import com.znkj.rchl_hz.utils.getInfoUtils;
import com.znkj.rchl_hz.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class SearchInfoActivity extends AppCompatActivity{

    private Button backBtn;
    private TextView title;

    private RecyclerView recyclerView;
    private SearchView searchView;
    private LinearLayout llSearchListView;
    private FuzzMatchPersonAdapter adapter;
    private List personBeans;
    private PersonBean person = null;
    private String codeType = "";

    private getInfoUtils getInfo;

    private static final String TAG = "SearchInfoActivity";
    private Dialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_info_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);
        initView();
        initData();
    }

    public void initView(){
        Intent intent = getIntent();
        title = (TextView)findViewById(R.id.text_title);
        if(intent.getStringExtra("typeName")!=null&&
                !"".equalsIgnoreCase(intent.getStringExtra("typeName").trim())){
            title.setText(intent.getStringExtra("typeName"));
            codeType = intent.getStringExtra("type").trim();
        }else {
            title.setText("选择代码项");
            codeType = "error";
        }
        backBtn = (Button)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SearchInfoActivity.this.finish();
            }
        });
        llSearchListView = (LinearLayout) findViewById(R.id.ll_search_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_search);
        searchView = (SearchView) findViewById(R.id.sv_input);
        this.initSearchView();
        this.initRecycleView();
    }

    private void initSearchView() {
        searchView.setSearchViewListener(new SearchView.onSearchViewListener() {
            @Override
            public boolean onQueryTextChange(String text) {
                llSearchListView.setVisibility(View.VISIBLE);
                updateSelectList(text);
                return false;
            }
        });
    }

    private void initRecycleView() {
        adapter = new FuzzMatchPersonAdapter(this, new ArrayList<PersonBean>());
        //设置recycleView 的布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置recycleView item之间的分割线
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(Color.parseColor("#f3f3f3")).size(3).build());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                person = adapter.getItemData(position);
                returnSearchViewResult();
            }
        });
    }

    private void updateSelectList(String text) {
        adapter.removeAllItems();
        List<PersonBean> personBeans = searchItems(text);
        adapter.addItems(personBeans);
    }

    public void returnSearchViewResult() {
        Intent intentTemp = new Intent();
        //返回选择的参数
        switch(codeType) {
            case "hjd":
                fragmentTag fInfo =  fragmentTag.getFtTag();
                fInfo.setDm(person.getDm());
                fInfo.setName(person.getName());
                break;
            case "gjdq":
                intentTemp.putExtra("name",person.getName());
                intentTemp.putExtra("dm",person.getDm());
                setResult(5,intentTemp);
                break;
            case "zjzl":
                intentTemp.putExtra("name",person.getName());
                intentTemp.putExtra("dm",person.getDm());
                setResult(6,intentTemp);
                break;
            default:
                break;
        }
        SearchInfoActivity.this.finish();

    }

    public List<PersonBean> searchItems(String name) {
        ArrayList<PersonBean> mSearchList = new ArrayList<>();
        for (int i = 0; i < personBeans.size(); i++) {
            int index = ((PersonBean)personBeans.get(i)).getName().indexOf(name);
            int index2 = ((PersonBean)personBeans.get(i)).getDm().indexOf(name);
            // 存在匹配的数据
            if (index != -1||index2 != -1) {
                mSearchList.add((PersonBean)personBeans.get(i));
            }
        }
        return mSearchList;
    }

    private void initData() {
        String dateStr ="";
        JSONObject dateJson = null;
        JSONObject tempJson = null;
        JSONArray jsArr = null;
//        if(codeType.equalsIgnoreCase("hjd")){
//
//        }
        switch(codeType) {
            case "hjd":
                dateStr = getInfo.getUserInfo(this,"hjdList");
                break;
            case "gjdq":
                dateStr = getInfo.getUserInfo(this,"gjdqList");
                break;
            case "zjzl":
                //暂时屏蔽
                dateStr = getInfo.getUserInfo(this,"zjzlList");
                break;
            default:
                break;
        }

        personBeans = new ArrayList();
        if(!"".equalsIgnoreCase(dateStr.trim())){
            try {
                dateJson = new JSONObject(dateStr);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else {Toast.makeText(SearchInfoActivity.this, "date is null", Toast.LENGTH_LONG).show();}
        if(dateJson!=null){
            try {
                //Log.e("jjjjjjjjj","==="+dateJson.get("res"));
                jsArr = (JSONArray) dateJson.get("res");
                for (int j=0; j<jsArr.length(); j++){
                    tempJson = (JSONObject)jsArr.get(j);

                    //Log.e("ssss","==="+tempJson.get("name"));
                    //Log.e("ssss","==="+tempJson.get("dm"));
                    try {
                        PersonBean personBean = new PersonBean(""+URLDecoder.decode((String)tempJson.get("name"),"UTF-8"),""+URLDecoder.decode((String)tempJson.get("dm")));
                        personBeans.add(personBean);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }else {
            //Toast.makeText(SearchInfoActivity.this, "dateJson is null", Toast.LENGTH_LONG).show();
            String[] names = {"代码获取错误", "代码获取错误", "代码获取错误"};
            String[] sex = {"999999", "999999"};

            for (int i = 0; i < 10; i++) {
                PersonBean personBean = new PersonBean(names[i % 3], sex[i % 2]);
                personBeans.add(personBean);
            }
        }

    }
}
