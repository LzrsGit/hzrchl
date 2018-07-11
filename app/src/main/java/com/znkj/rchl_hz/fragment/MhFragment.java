package com.znkj.rchl_hz.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.activity.ResultActivity;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.getInfoUtils;


public class MhFragment extends Fragment {

    private Button addBtn;
    private EditText mhhc_xm;
    private EditText mhhc_sjh;
    private EditText mhhc_hjd;
    private EditText mhhc_mz;
    private EditText mhhc_kssj;
    private EditText mhhc_jzsj;

    private getInfoUtils getInfo;
    private final int CODE = 1;


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
            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mh_fragment, container, false);

        initView(view);
        addBtn = (Button) view.findViewById(R.id.mhhc_add);
        //add_cancel = (Button) view.findViewById(R.id.add_cancel);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //暂时屏蔽
                //SubmitInfo();
                Intent intent = new Intent(getActivity(),
                        ResultActivity.class);
                startActivity(intent);
            }

        });

        //点击其他空白处隐藏软键盘
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus().getWindowToken()!=null){
                        manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

        return view;
    }

    // 控件的初始化
    private void initView(View v){
        mhhc_sjh = (EditText)v.findViewById(R.id.mhhc_sjh);
        mhhc_xm = (EditText)v.findViewById(R.id.mhhc_xm);
        mhhc_hjd = (EditText)v.findViewById(R.id.mhhc_hjd);
        mhhc_mz = (EditText)v.findViewById(R.id.mhhc_mz);
        mhhc_kssj = (EditText)v.findViewById(R.id.mhhc_csrq_s);
        mhhc_jzsj = (EditText)v.findViewById(R.id.mhhc_csrq_e);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void SubmitInfo() {

        if (mhhc_sjh.getText().toString().equals("")
                || mhhc_xm.getText().toString().equals("")
                || mhhc_hjd.getText().toString().equals("")
                || mhhc_mz.getText().toString().equals("")
                || mhhc_kssj.getText().toString().equals("")
                || mhhc_jzsj.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "您输入的信息不完整！",
                    Toast.LENGTH_SHORT).show();
        } else {

            try {
                HcInfo hcInfo = new HcInfo();
                hcInfo.setHclx("mh");
                hcInfo.setHcbb(getInfo.getUserInfo(getContext(),"sys_bb"));
                hcInfo.setHcr_xm(getInfo.getUserInfo(getContext(),"use_name"));
                hcInfo.setHcr_sjh(getInfo.getUserInfo(getContext(),"use_phonenum"));
                hcInfo.setHcr_sfzh(getInfo.getUserInfo(getContext(),"use_idcard"));
                hcInfo.setHcdz(getInfo.getAddress(getContext()));
                hcInfo.setBhcr_sjh(mhhc_sjh.getText().toString());
                hcInfo.setBhcr_xm(mhhc_xm.getText().toString());
                hcInfo.setBhcr_mz(mhhc_mz.getText().toString());
                hcInfo.setBhcr_hjszd(mhhc_hjd.getText().toString());
                hcInfo.setBhcr_csrqks(mhhc_kssj.getText().toString());
                hcInfo.setBhcr_csrqjs(mhhc_jzsj.getText().toString());
                hcInfo.setBhcr_jnjw("01");
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
}
