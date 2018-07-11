package com.znkj.rchl_hz.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.MainActivity;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.activity.ResultActivity;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.utils.DialogUtils;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.getInfoUtils;

/**
 * Created by Carson_Ho on 16/7/22.
 */
public class JqFragment extends Fragment {

    private Button addBtn;
    private EditText jqhc_sjh;
    private EditText jqhc_sfzh;

    private getInfoUtils getInfo;
    private final int CODE = 1;

    private Dialog mDialog;


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:  //访问成功，有数据
                    Intent intent = new Intent(getActivity(),
                            ResultActivity.class);
                    intent.putExtra("result", msg.obj.toString());
                    startActivity(intent);
                    break;
                case 0:
                    String result = msg.obj.toString();
                    result = "提交失败！";
                    //Log.e("bbbbuuuuuggggg","====="+msg.obj.toString());
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            DialogUtils.closeDialog(mDialog);

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jq_fragment, container, false);

        initView(view);
        addBtn = (Button) view.findViewById(R.id.jq_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SubmitInfo();
                //mHandler.sendEmptyMessageDelayed(1, 2000);
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
        jqhc_sjh = (EditText) v.findViewById(R.id.jqhc_sjh);
        jqhc_sfzh = (EditText) v.findViewById(R.id.jqhc_sfzh);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void SubmitInfo() {

        if (jqhc_sjh.getText().toString().trim().equals("")
                || jqhc_sfzh.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "您输入的信息不完整！",
                    Toast.LENGTH_SHORT).show();
        } else {
            mDialog = DialogUtils.createLoadingDialog(getActivity(),"加载中...");
            try {
                HcInfo hcInfo = new HcInfo();
                hcInfo.setHclx("jq");
                hcInfo.setHcbb(getInfo.getUserInfo(getContext(),"sys_bb"));
                hcInfo.setHcr_xm(getInfo.getUserInfo(getContext(),"use_name"));
                hcInfo.setHcr_sjh(getInfo.getUserInfo(getContext(),"use_phonenum"));
                hcInfo.setHcr_sfzh(getInfo.getUserInfo(getContext(),"use_idcard"));
                hcInfo.setHcdz(getInfo.getAddress(getContext()));
                hcInfo.setBhcr_sjh(jqhc_sjh.getText().toString());
                hcInfo.setBhcr_sfzh(jqhc_sfzh.getText().toString());
                hcInfo.setBhcr_jnjw("01");
                //String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
                //mDialog = DialogUtils.createLoadingDialog(getActivity(),"加载中...");
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
