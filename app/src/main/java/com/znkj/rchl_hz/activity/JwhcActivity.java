package com.znkj.rchl_hz.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.utils.ActPhotoUtils;
import com.znkj.rchl_hz.utils.DialogUtils;
import com.znkj.rchl_hz.utils.HttpUtil;
import com.znkj.rchl_hz.utils.PhotoUtils;
import com.znkj.rchl_hz.utils.ToBitmapUtils;
import com.znkj.rchl_hz.utils.ToastUtils;
import com.znkj.rchl_hz.utils.getInfoUtils;
import com.znkj.rchl_hz.widget.BottomMenuDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class JwhcActivity extends AppCompatActivity implements View.OnClickListener{

    private Button backBtn;
    private Button jnhcBtn;
    private Button addBtn;
    private TextView title;
    private TextView bottom;

    private EditText gjdq;
    private EditText zjzl;
    private EditText zjhm;
    private ImageView photo1;
    private ImageView photo2;

    private String gjdqDm ="";
    private String zjzlDm ="";

    String encodeString = "";
    String encodeString2 = "";
    private String[] localArr = {"",""};

    private getInfoUtils getInfo;

    private static final String TAG = "jwhcActivity";
    private static String ImgId = "id1";
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int CODE_CAMERA_NOCROP_REQUEST = 0xa5;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/myPhoto");
    //private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo");
    private Uri imageUri;
    private Dialog mDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jwhc_layout);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#1FA0FE") , false);
        initView();
        gjdq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSelectActivity("gjdq");
            }
        });
        zjzl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSelectActivity("zjzl");
            }
        });
        photo1.setOnClickListener(new View.OnClickListener() {
            BottomMenuDialog d5;
            @Override
            public void onClick(View v) {
                d5 = new BottomMenuDialog.Builder(JwhcActivity.this)
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
                        }).create();
                d5.show();
            }
        });
        photo2.setOnClickListener(new View.OnClickListener() {
            BottomMenuDialog d5;
            @Override
            public void onClick(View v) {
                d5 = new BottomMenuDialog.Builder(JwhcActivity.this)
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
                        }).create();
                d5.show();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initView(){
        title = (TextView)findViewById(R.id.text_title);
        title.setText("境外人员");
        bottom = (TextView)findViewById(R.id.bottom_address);
        bottom.setText(getInfo.getAddress(this));
        gjdq = (EditText)findViewById(R.id.jwhc_gjdq);
        zjzl = (EditText)findViewById(R.id.jwhc_zjzl);
        zjhm = (EditText)findViewById(R.id.jwhc_zjhm);
        gjdq.setInputType(InputType.TYPE_NULL);//不弹出键盘
        zjzl.setInputType(InputType.TYPE_NULL);//不弹出键盘
        backBtn = (Button)findViewById(R.id.back_btn);
        addBtn = (Button)findViewById(R.id.jw_add_btn);
        jnhcBtn = (Button)findViewById(R.id.jw_btn);
        Drawable drawable = getDrawable(R.drawable.ic_jn);
        jnhcBtn.setBackground(drawable);
        photo1 = (ImageView)findViewById(R.id.photo1);
        photo2 = (ImageView)findViewById(R.id.photo2);
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        jnhcBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.back_btn:
                JwhcActivity.this.finish();
                break;
            case R.id.jw_btn:
                Intent intent = new Intent(JwhcActivity.this,MidActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.jw_add_btn:
                //暂时屏蔽
                SubmitInfo();
                break;
            default:
                break;
        }
    }

    //新activity选择方法
    public void openSelectActivity(String eText) {
        Intent intent = new Intent();
        //SoilsenerActivity.class为想要跳转的Activity
        intent.setClass(this, SearchInfoActivity.class);
        intent.putExtra("type",eText);
        if(eText.equalsIgnoreCase("gjdq")){
            intent.putExtra("typeName","国籍地区");
        }else if(eText.equalsIgnoreCase("zjzl")){
            intent.putExtra("typeName","证件种类");
        }
        this.startActivityForResult(intent, 0);
    }

    /**
     * 动态申请sdcard读写权限
     */
    private void autoObtainStoragePermission(String imgId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(JwhcActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                ImgId = imgId;
                ActPhotoUtils.openPic(JwhcActivity.this, CODE_GALLERY_REQUEST);
            }
        } else {
            ImgId = imgId;
            ActPhotoUtils.openPic(JwhcActivity.this, CODE_GALLERY_REQUEST);
        }
    }

    /**
     * 申请访问相机权限
     */
    private void autoObtainCameraPermission(String imgId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(JwhcActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(JwhcActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(JwhcActivity.this, Manifest.permission.CAMERA)) {
                    ToastUtils.showShort(JwhcActivity.this, "您已经拒绝过一次");
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
                        imageUri = FileProvider.getUriForFile(JwhcActivity.this, "com.znkj.rchl_hz.provider.fileprovider", imageFile);
                    }
                    ImgId = imgId;
                    ActPhotoUtils.takePicture(JwhcActivity.this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    ToastUtils.showShort(JwhcActivity.this, "设备没有SD卡！");
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
        Bitmap bitmap = tobit.compressBitmap(JwhcActivity.this,uri,ph);
        //Bitmap bitmap = PhotoUtils.getBitmapFromUri(uri, getActivity());
        if (bitmap != null) {
            showImages(bitmap,ph);
            if("id1".equalsIgnoreCase(imgId)){
                photo2.setVisibility(View.VISIBLE);
                mDialog = DialogUtils.createLoadingDialog(JwhcActivity.this,"图片处理中...");
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
                mDialog = DialogUtils.createLoadingDialog(JwhcActivity.this,"图片处理中...");
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
        if(resultCode==5){
            //国籍地区返回
            gjdq.setText(data.getStringExtra("name"));
            gjdqDm = data.getStringExtra("dm");
        }else if(resultCode==6){
            //证件种类返回：
            zjzl.setText(data.getStringExtra("name"));
            zjzlDm = data.getStringExtra("dm");
        }else if (resultCode != RESULT_OK) {
            Log.e(TAG, "onActivityResult: resultCode!=RESULT_OK");
            return;
        }
        Log.e(TAG, "onActivityResult: resultCode===="+resultCode);
        switch (requestCode) {
            //相机返回
            case CODE_CAMERA_REQUEST:
                getMyBitmap(imageUri,ImgId);
                break;
            //相册返回
            case CODE_GALLERY_REQUEST:

                if (hasSdcard()) {
                    Uri newUri = Uri.parse(PhotoUtils.getPath(JwhcActivity.this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(JwhcActivity.this, "com.znkj.rchl_hz.provider.fileprovider", new File(newUri.getPath()));
                    }
                    //裁剪照片
                    //PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    //不裁剪图片
                    getMyBitmap(newUri,ImgId);
                } else {
                    ToastUtils.showShort(JwhcActivity.this, "设备没有SD卡！");
                }
                break;
            //裁剪返回
            case CODE_RESULT_REQUEST:
                break;
            default:
        }
    }

    public void SubmitInfo() {
        if (gjdqDm.trim().equalsIgnoreCase("")
                || zjzlDm.trim().equalsIgnoreCase("")
                || zjhm.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(JwhcActivity.this, "您输入的信息不完整！",
                    Toast.LENGTH_SHORT).show();
        } else {
            mDialog = DialogUtils.createLoadingDialog(JwhcActivity.this,"提交中...");
            try {
                HcInfo hcInfo = new HcInfo();
                hcInfo.setSjlx("hc");
                hcInfo.setHclx("jw");
                hcInfo.setHcbb(getInfo.getUserInfo(this,"sys_bb"));
                hcInfo.setHcr_xm(getInfo.getUserInfo(this,"use_name"));
                hcInfo.setHcr_sjh(getInfo.getUserInfo(this,"use_phonenum"));
                hcInfo.setHcr_sfzh(getInfo.getUserInfo(this,"use_idcard"));
                hcInfo.setHcdz(getInfo.getAddress(this));
//                localArr = getInfo.getLocal(this,localArr);
//                hcInfo.setHcjd(localArr[0]);
//                hcInfo.setHcwd(localArr[1]);
                hcInfo.setBhcr_jnjw("02");
                hcInfo.setBhcr_zjlx(zjzlDm);
                hcInfo.setBhcr_sfzh(zjhm.getText().toString());
                hcInfo.setBhcr_hjszd(gjdqDm);
                hcInfo.setBhcr_zp(encodeString);
                hcInfo.setBhcr_zp2(encodeString2);
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

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:  //图片编译成功
                    Toast.makeText(JwhcActivity.this, "图片编译成功", Toast.LENGTH_LONG).show();
                    break;
                case 1:  //访问成功，有数据
                    Intent intent = new Intent(JwhcActivity.this,
                            ResultActivity.class);
                    intent.putExtra("result", msg.obj.toString());
                    startActivity(intent);
                    break;
                case 0:
                    //String result = msg.obj.toString();
                    String result = "提交失败！";
                    Toast.makeText(JwhcActivity.this, result, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            DialogUtils.closeDialog(mDialog);
        }
    };
}
