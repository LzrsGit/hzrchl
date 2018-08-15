package com.znkj.rchl_hz.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.text.method.NumberKeyListener;
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
import android.widget.TextView;
import android.widget.Toast;

import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.MainActivity;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.activity.ResultActivity;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.utils.CheckUtils;
import com.znkj.rchl_hz.utils.DialogUtils;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.PhotoUtils;
import com.znkj.rchl_hz.utils.ToBitmapUtils;
import com.znkj.rchl_hz.utils.ToastUtils;
import com.znkj.rchl_hz.utils.getInfoUtils;
import com.znkj.rchl_hz.widget.BottomMenuDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Carson_Ho on 16/7/22.
 */
public class JqFragment extends Fragment {

    private Button addBtn;
    private EditText jqhc_sjh;
    private EditText jqhc_sfzh;
    private TextView dj_sfzh;
    private ImageView photo1;
    private ImageView photo2;

    String encodeString = "";
    String encodeString2 = "";
    private String[] localArr = {"",""};
    //private ImageView photo3;
    //private int sfxs=0;

    private getInfoUtils getInfo;
    private final int CODE = 1;

    private static final String TAG = "jqFragment";
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
                    //photo3.setVisibility(View.VISIBLE);
                    //base64ToBitmap(encodeString,photo3);
                    Toast.makeText(getActivity(), "图片编译成功", Toast.LENGTH_LONG).show();
                    //Log.e("pp===",encodeString);
                    //Log.e("p2p===",encodeString2);
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
                    //String result = msg.obj.toString();
                    String result = "提交失败！";
                    //Log.e("bbbbuuuuuggggg","====="+msg.obj.toString());
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            DialogUtils.closeDialog(mDialog);

        }
    };


    /*通过NFC读取身份证信息成功*/
    public static final String ACTION_NFC_READ_IDCARD_SUCCESS = "cybertech.pstore.intent.action.NFC_READ_IDCARD_SUCCESS";
    /*通过NFC读取身份证信息失败*/
    public static final String ACTION_NFC_READ_IDCARD_FAILURE = "cybertech.pstore.intent.action.NFC_READ_IDCARD_FAILURE";
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        register();
    }
    public void onDestroy() {
        super.onDestroy();
        unregister();
    }
    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NFC_READ_IDCARD_SUCCESS);
        filter.addAction(ACTION_NFC_READ_IDCARD_FAILURE);
        getContext().registerReceiver(mReceiver, filter);
    }
    private void unregister() {
        getContext().unregisterReceiver(mReceiver);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle data = intent.getExtras();
            Log.i(TAG, "onReceive: " + action + ", " + data);
            if (ACTION_NFC_READ_IDCARD_SUCCESS.equals(action)) {
                // TODO: 获取身份证数据成功
                showContent2(data);
            } else if (ACTION_NFC_READ_IDCARD_FAILURE.equals(action)) {
                // TODO: 获取身份证数据失败
                Toast.makeText(getActivity(), "读取身份信息失败，请手动输入身份证号", Toast.LENGTH_LONG).show();
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
            jqhc_sfzh.setText(sb.toString());
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jq_fragment, container, false);
        initView(view);

        dj_sfzh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("cybertech.pstore.intent.action.NFC_READER");
                intent.setPackage("cn.com.cybertech.nfc.reader");
                startActivity(intent);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SubmitInfo();
                //mHandler.sendEmptyMessageDelayed(1, 2000);
            }
        });
        jqhc_sfzh.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'X','x' };
                return numberChars;
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
        jqhc_sjh = (EditText) v.findViewById(R.id.jqhc_sjh);
        jqhc_sfzh = (EditText) v.findViewById(R.id.jqhc_sfzh);
        dj_sfzh = (TextView) v.findViewById(R.id.dj_sfzh);
        photo1 = (ImageView)v.findViewById(R.id.photo1);
        photo2 = (ImageView)v.findViewById(R.id.photo2);
        //photo3 = (ImageView)v.findViewById(R.id.photo3);
        addBtn = (Button) v.findViewById(R.id.jq_add_btn);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void clearAll(){
        jqhc_sjh.setText("");
        jqhc_sfzh.setText("");
        clearImageview("id2");
        clearImageview("id1");
    }

    public void SubmitInfo() {

        if (//jqhc_sjh.getText().toString().trim().equals("")
                jqhc_sfzh.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "您输入的信息不完整！",
                    Toast.LENGTH_SHORT).show();
        }else {
            if (CheckUtils.IDCardValidate(jqhc_sfzh.getText().toString().trim())
                    &&CheckUtils.isChinaPhoneLegal(jqhc_sjh.getText().toString().trim())) {
                mDialog = DialogUtils.createLoadingDialog(getActivity(), "提交中...");
                try {
                    HcInfo hcInfo = new HcInfo();
                    hcInfo.setSjlx("hc");
                    hcInfo.setHclx("jq");
                    hcInfo.setHcbb(getInfo.getUserInfo(getContext(), "sys_bb"));
                    hcInfo.setHcr_xm(getInfo.getUserInfo(getContext(), "use_name"));
                    hcInfo.setHcr_sjh(getInfo.getUserInfo(getContext(), "use_phonenum"));
                    hcInfo.setHcr_sfzh(getInfo.getUserInfo(getContext(), "use_idcard"));
                    hcInfo.setHcdz(getInfo.getAddress(getContext()));
//                    localArr = getInfo.getLocal(getContext(), localArr);
//                    hcInfo.setHcjd(localArr[0]);
//                    hcInfo.setHcwd(localArr[1]);
                    hcInfo.setBhcr_sjh(jqhc_sjh.getText().toString());
                    hcInfo.setBhcr_sfzh(jqhc_sfzh.getText().toString().toUpperCase());
                    hcInfo.setBhcr_zp(encodeString);
                    hcInfo.setBhcr_zp2(encodeString2);
                    hcInfo.setBhcr_jnjw("01");
                    HttpUtil.sendHcRequest(this.getString(R.string.url_address), hcInfo, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Message message = new Message();
                            if ("WRONG".equalsIgnoreCase(response.trim())) {
                                message.what = 0;
                            } else {
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
                Toast.makeText(getActivity(), "身份证号/手机号校检错误",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

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
                        //Log.e("encodeString:",""+encodeString.length());
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
                        //Log.e("encodeString2:",""+encodeString2.length());
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

//    private void base64ToBitmap(String base64Data,ImageView view) {
//        String get = base64Data.replace(" ", "+");
//        byte[] bytes = Base64.decode(get, Base64.DEFAULT);
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = false;
//        opts.inSampleSize = 2;
//        Bitmap bitmap = null;
//
//        bitmap = (Bitmap) BitmapFactory.decodeByteArray(bytes,0,bytes.length,opts);
//        if (bitmap!=null){
//            view.setImageBitmap(bitmap);
//        }
//
//        if (bytes != null) {
//            bytes = null;
//        }
//
//    }


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
        //销毁图片
//        BitmapDrawable drawable = (BitmapDrawable)view.getDrawable();
//        Bitmap bmp = drawable.getBitmap();
//        if (null != bmp && !bmp.isRecycled()){
//            bmp.recycle();
//            bmp = null;
//        }
        //设置为默认图片
        Bitmap gameStatusBitmap =BitmapFactory.decodeResource(getResources(), R.drawable.addphoto);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "onActivityResult: resultCode!=RESULT_OK");
            return;
        }
        switch (requestCode) {
            //相机返回
            case CODE_CAMERA_REQUEST:
//                if (!fileCropUri.exists()) {
//                    fileCropUri.mkdirs();
//                }
//                File imageFile = new File(fileCropUri, PhotoUtils.getCharacterAndNumber() + ".jpg");
//                cropImageUri = Uri.fromFile(imageFile);
                //裁剪照片
                //PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                //不裁剪图片

                getMyBitmap(imageUri,ImgId);
                break;
            //相册返回
            case CODE_GALLERY_REQUEST:

                if (hasSdcard()) {
//                    if (!fileCropUri.exists()) {
//                        fileCropUri.mkdirs();
//                    }
//                    File imageFile2 = new File(fileCropUri, PhotoUtils.getCharacterAndNumber() + ".jpg");
//                    cropImageUri = Uri.fromFile(imageFile2);
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
//                getMyBitmap(cropImageUri,ImgId);
                break;
            default:
        }
    }
}
