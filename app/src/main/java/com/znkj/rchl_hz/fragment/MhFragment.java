package com.znkj.rchl_hz.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.activity.ResultActivity;
import com.znkj.rchl_hz.activity.SearchInfoActivity;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.model.fragmentTag;
import com.znkj.rchl_hz.utils.CheckUtils;
import com.znkj.rchl_hz.utils.DialogUtils;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.PhotoUtils;
import com.znkj.rchl_hz.utils.ToBitmapUtils;
import com.znkj.rchl_hz.utils.ToastUtils;
import com.znkj.rchl_hz.utils.getInfoUtils;
import com.znkj.rchl_hz.widget.BottomMenuDialog;
import com.znkj.rchl_hz.widget.SerachSelectDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MhFragment extends Fragment {

    private Button addBtn;
    private EditText mhhc_xm;
    private EditText mhhc_sjh;
    private EditText mhhc_hjd;
    private EditText mhhc_mz;
    private EditText mhhc_kssj;
    private EditText mhhc_jzsj;
    private ImageView photo1;
    private ImageView photo2;

    String encodeString = "";
    String encodeString2 = "";
    private String[] localArr = {"",""};

    private List<String> mzDatas;
    private String[] mzArr = {"",""};
    private String mzDm="";
    private String mzMc="";
    private String hjdDm="";
    private String hjdMc="";

    private getInfoUtils getInfo;
    private final int CODE = 1;

    private static final String TAG = "mhFragment";
    private static String ImgId = "id1";
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int CODE_CAMERA_NOCROP_REQUEST = 0xa5;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/myPhoto");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo");
    private Uri imageUri;
    //    private Uri cropImageUri;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    private Dialog mDialog;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:  //图片编译成功
                    Toast.makeText(getActivity(), "图片编译成功", Toast.LENGTH_LONG).show();
                    break;
                case 1:  //访问成功，有数据
                    //清除数据
                    clearAll();
                    Intent intent = new Intent(getActivity(),
                            ResultActivity.class);
                    intent.putExtra("result", msg.obj.toString());
                    startActivity(intent);
                    break;
                case 0:
                    String result = "提交失败！";
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
        View view = inflater.inflate(R.layout.mh_fragment, container, false);

        initView(view);
        initData();
        //add_cancel = (Button) view.findViewById(R.id.add_cancel);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //暂时屏蔽
                SubmitInfo();
//                Intent intent = new Intent(getActivity(),
//                        ResultActivity.class);
//                startActivity(intent);
            }

        });
        mhhc_mz.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               openSelectDialog(mhhc_mz);
           }
         });
        mhhc_hjd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSelectActivity("hjd");
            }
        });
        mhhc_kssj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date2, View v) {//选中事件回调
                        String time = getTime(date2);
                        mhhc_kssj.setText(time);
                    }
                })
                        .setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setContentSize(20)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        //                        .setTitleText("请选择时间")//标题文字
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                        .setCancelColor(Color.BLUE)//取消按钮文字颜色
                        // .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                        // .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                        // .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20)//默认是1900-2100年
                        // .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                        // .setRangDate(startDate,endDate)//起始终止年月日设定
                        // .setLabel("年","月","日","时","分","秒")
                        //.isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        // .isDialog(true)//是否显示为对话框样式
                        .build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();

            }
        });
        mhhc_jzsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date2, View v) {//选中事件回调
                        String time = getTime(date2);
                        mhhc_jzsj.setText(time);
                    }
                })
                        .setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setContentSize(20)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        //                        .setTitleText("请选择时间")//标题文字
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                        .setCancelColor(Color.BLUE)//取消按钮文字颜色
                        // .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                        // .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                        // .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20)//默认是1900-2100年
                        // .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                        // .setRangDate(startDate,endDate)//起始终止年月日设定
                        // .setLabel("年","月","日","时","分","秒")
                        //.isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        // .isDialog(true)//是否显示为对话框样式
                        .build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();

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

        photo1.setOnClickListener(new View.OnClickListener() {
            BottomMenuDialog d5;
            @Override
            public void onClick(View v) {
                d5 = new BottomMenuDialog.Builder(getActivity())
                        .setTitle("选择照片")
                        .addMenu("从手机相册选择", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d5.dismiss();
                                //Toast.makeText(v.getContext(), "从手机相册选择" , Toast.LENGTH_SHORT).show();
                                autoObtainStoragePermission("id1");
                            }
                        }).addMenu("拍照", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d5.dismiss();
                                //Toast.makeText(v.getContext(), "拍一张" , Toast.LENGTH_SHORT).show();
                                autoObtainCameraPermission("id1");
                            }
                        }).addMenu("取消选择", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d5.dismiss();
                                clearImageview("id1");
                            }
                        }).create();
                //photo2.setVisibility(View.VISIBLE);
                d5.show();

            }
        });
        photo2.setOnClickListener(new View.OnClickListener() {
            BottomMenuDialog d5;
            @Override
            public void onClick(View v) {
                d5 = new BottomMenuDialog.Builder(getActivity())
                        .setTitle("选择照片")
                        .addMenu("从手机相册选择", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d5.dismiss();
                                //Toast.makeText(v.getContext(), "从手机相册选择" , Toast.LENGTH_SHORT).show();
                                autoObtainStoragePermission("id2");
                            }
                        }).addMenu("拍照", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d5.dismiss();
                                //Toast.makeText(v.getContext(), "拍一张" , Toast.LENGTH_SHORT).show();
                                autoObtainCameraPermission("id2");
                            }
                        }).addMenu("取消选择", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d5.dismiss();
                                clearImageview("id2");
                            }
                        }).create();

                d5.show();
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
        mhhc_mz.setInputType(InputType.TYPE_NULL);//不弹出键盘
        mhhc_kssj.setInputType(InputType.TYPE_NULL);//不弹出键盘
        mhhc_jzsj.setInputType(InputType.TYPE_NULL);//不弹出键盘
        photo1 = (ImageView)v.findViewById(R.id.photo1);
        photo2 = (ImageView)v.findViewById(R.id.photo2);
        addBtn = (Button) v.findViewById(R.id.mhhc_add);

    }
    private void initData() {
        mzDatas = new ArrayList<>();
        String[] mzs = {"01:汉族", "02:蒙古族", "03:回族", "04:藏族", "05:维吾尔族", "06:苗族", "07:彝族",
        "08:壮族", "09:布依族", "10:朝鲜族", "11:满族", "12:侗族", "13:瑶族", "14:白族",
        "15:土家族", "16:哈尼族", "17:哈萨克族", "18:傣族", "19:黎族", "20:傈僳族", "21:佤族",
        "22:畲族", "23:高山族", "24:拉祜族", "25:水族", "26:东乡族", "27:纳西族", "28:景颇族",
        "29:柯尔克孜族", "30:土族", "31:达斡尔族", "32:仫佬族", "33:羌族", "34:布朗族", "35:撒拉族",
        "36:毛南族", "37:仡佬族", "38:锡伯族", "39:阿昌族", "40:普米族", "41:塔吉克族", "42:怒族",
        "43:乌孜别克族", "44:俄罗斯族", "45:鄂温克族", "46:德昂族", "47:保安族", "48:裕固族", "49:京族",
        "50:塔塔尔族", "51:独龙族", "52:鄂伦春族", "53:赫哲族", "54:门巴族", "55:珞巴族", "56:基诺族",
        "97:其他", "98:外国血统 中国籍人士"};
        for (int j = 0; j < mzs.length; j++) {
            mzDatas.add(mzs[j]);
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //弹框选择方法
    public void openSelectDialog(EditText eText) {
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(getContext());
        alert.setListData(mzDatas);
        //alert.setTitle("请选择名族");
        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(info!=null&&!("".equalsIgnoreCase(info.trim()))){
                    mzArr = info.split(":");
                    mzDm = mzArr[0];
                    mzMc = mzArr[1];
                }
                eText.setText(mzMc);
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9, 0.9, getActivity());
    }
    //新activity选择方法
    public void openSelectActivity(String eText) {
        Intent intent = new Intent();
        //SoilsenerActivity.class为想要跳转的Activity
        intent.setClass(getActivity(), SearchInfoActivity.class);
        intent.putExtra("type",eText);
        intent.putExtra("typeName","户籍地区划");
        this.startActivityForResult(intent, 0);
    }

    //调用相机相册方法开始
    /**
     * 动态申请sdcard读写权限
     */
    private void autoObtainStoragePermission(String imgId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                ImgId = imgId;
                PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
            }
        } else {
            ImgId = imgId;
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }
    }

    /**
     * 申请访问相机权限
     */
    private void autoObtainCameraPermission(String imgId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    ToastUtils.showShort(getActivity(), "您已经拒绝过一次");
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                if (hasSdcard()) {
                    if (!fileUri.exists()) {
                        fileUri.mkdirs();
                    }
                    File imageFile = new File(fileUri, PhotoUtils.getCharacterAndNumber() + ".jpg");
                    imageUri = Uri.fromFile(imageFile);
                    //通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(getActivity(), "com.znkj.rchl_hz.provider.fileprovider", imageFile);
                    }
                    ImgId = imgId;
                    PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    ToastUtils.showShort(getActivity(), "设备没有SD卡！");
                }
            }
        }
    }

    private void getMyBitmap(Uri uri, String imgId){
        ToBitmapUtils tobit = new ToBitmapUtils();
        ImageView ph = photo1;
        switch (imgId){
            case "id1":
                ph = photo1;
                break;
            case "id2":
                ph = photo2;
                break;
            default:
        }
        Bitmap bitmap = tobit.compressBitmap(getActivity(),uri,ph);
        //Bitmap bitmap = PhotoUtils.getBitmapFromUri(uri, getActivity());
        if (bitmap != null) {
            showImages(bitmap,ph);
            if("id1".equalsIgnoreCase(imgId)){
                photo2.setVisibility(View.VISIBLE);
                mDialog = DialogUtils.createLoadingDialog(getActivity(),"图片处理中...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 写子线程中的操作
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                        byte[] bytes = baos.toByteArray();

                        //base64 encode
                        byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
                        encodeString = new String(encode);
                        //Log.e("encodeString:",encodeString);
                        Message msg = new Message();
                        if(encodeString!=null&&!"".equalsIgnoreCase(encodeString)){
                            msg.what = 2;

                        }else {
                            msg.what = 0;
                        }
                        mHandler.sendMessage(msg);
                    }
                }).start();

            }else if("id2".equalsIgnoreCase(imgId)){
                mDialog = DialogUtils.createLoadingDialog(getActivity(),"图片处理中...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 写子线程中的操作
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                        byte[] bytes = baos.toByteArray();

                        //base64 encode
                        byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
                        encodeString2 = new String(encode);
                        //Log.e("encodeString:",encodeString);
                        Message msg = new Message();
                        if(encodeString2!=null&&!"".equalsIgnoreCase(encodeString2)){
                            msg.what = 2;
                        }else {
                            msg.what = 0;
                        }
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        }
    }

    private void showImages(Bitmap bitmap,ImageView view) {
        //photo.setImageBitmap(bitmap);
        view.setImageBitmap(bitmap);
    }

    private void clearImageview(String imgId) {
        ImageView view = photo1;
        switch (imgId){
            case "id1":
                view = photo1;
                break;
            case "id2":
                view = photo2;
                break;
            default:
        }
        //设置为默认图片
        Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.addphoto);
        view.setImageBitmap(gameStatusBitmap);
        if(imgId.equalsIgnoreCase("id2")){
            encodeString2 = "";
        }else if(imgId.equalsIgnoreCase("id1")){
            encodeString = "";
        }

        //判断photo2是否显示添加照片
        if("".equalsIgnoreCase(encodeString.trim())&&"".equalsIgnoreCase(encodeString2.trim())){
            photo2.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState() ;
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode + "  resultCode:" + resultCode);

        //户籍地返回值无法获取暂时使用此方法代替
        fragmentTag fInfo =  fragmentTag.getFtTag();
        hjdDm = fInfo.getDm();
        hjdMc = fInfo.getName();
        mhhc_hjd.setText(hjdMc);
        fInfo.setDm("");
        fInfo.setName("");

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "onActivityResult: resultCode!=RESULT_OK");
            return;
        }
        switch (requestCode) {
            //获取户籍地返回
//            case 5:
//                Log.e("ccccccccccccccccccccc","=======");
//                hjdDm = data.getStringExtra("dm");
//                hjdMc = data.getStringExtra("name");
//                mhhc_hjd.setText(hjdMc);
//                break;
            //相机返回
            case CODE_CAMERA_REQUEST:
                //裁剪照片
                //PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                //不裁剪图片
                getMyBitmap(imageUri,ImgId);
                break;
            //相册返回
            case CODE_GALLERY_REQUEST:

                if (hasSdcard()) {
                    Uri newUri = Uri.parse(PhotoUtils.getPath(getActivity(), data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(getActivity(), "com.znkj.rchl_hz.provider.fileprovider", new File(newUri.getPath()));
                    }
                    //裁剪照片
                    //PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    //不裁剪图片
                    getMyBitmap(newUri,ImgId);
                } else {
                    ToastUtils.showShort(getActivity(), "设备没有SD卡！");
                }
                break;
            //裁剪返回
            case CODE_RESULT_REQUEST:
                break;
            default:
        }
    }
    //调用相机相册方法结束

    //时间选择器-显示时间
    public String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }
    //时间选择器-显示时间

    //清除数据
    public void clearAll(){
        mhhc_sjh.setText("");
        mhhc_hjd.setText("");
        mhhc_kssj.setText("");
        mhhc_jzsj.setText("");
        mhhc_xm.setText("");
        mhhc_mz.setText("");
        mzDm = "";
        mzMc = "";
        hjdMc = "";
        hjdDm = "";
        clearImageview("id2");
        clearImageview("id1");
    }
    //提交方法
    public void SubmitInfo() {
        if (//mhhc_sjh.getText().toString().equals("")
                mhhc_xm.getText().toString().equals("")
                ||mhhc_hjd.getText().toString().equals("")
                //|| mhhc_mz.getText().toString().equals("")
                || mhhc_kssj.getText().toString().equals("")
                || mhhc_jzsj.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "您输入的信息不完整！",
                    Toast.LENGTH_SHORT).show();
        } else {
            if(CheckUtils.isChinaPhoneLegal(mhhc_sjh.getText().toString().trim())){
                mDialog = DialogUtils.createLoadingDialog(getActivity(), "提交中...");
                try {
                    HcInfo hcInfo = new HcInfo();
                    hcInfo.setSjlx("hc");
                    hcInfo.setHclx("mh");
                    hcInfo.setHcbb(getInfo.getUserInfo(getContext(),"sys_bb"));
                    hcInfo.setHcr_xm(getInfo.getUserInfo(getContext(),"use_name"));
                    hcInfo.setHcr_sjh(getInfo.getUserInfo(getContext(),"use_phonenum"));
                    hcInfo.setHcr_sfzh(getInfo.getUserInfo(getContext(),"use_idcard"));
                    hcInfo.setHcdz(getInfo.getAddress(getContext()));
//                    localArr = getInfo.getLocal(getContext(),localArr);
//                    hcInfo.setHcjd(localArr[0]);
//                    hcInfo.setHcwd(localArr[1]);
                    hcInfo.setBhcr_sjh(mhhc_sjh.getText().toString());
                    hcInfo.setBhcr_xm(mhhc_xm.getText().toString());
                    hcInfo.setBhcr_mz(mzDm);
                    hcInfo.setBhcr_hjszd(hjdDm);
                    hcInfo.setBhcr_csrqks(mhhc_kssj.getText().toString());
                    hcInfo.setBhcr_csrqjs(mhhc_jzsj.getText().toString());
                    hcInfo.setBhcr_zp(encodeString);
                    hcInfo.setBhcr_zp2(encodeString2);
                    hcInfo.setBhcr_jnjw("01");
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
                    DialogUtils.closeDialog(mDialog);
                    Toast.makeText(getActivity(), "提交失败:"+e,
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getActivity(), "手机号校检错误",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
