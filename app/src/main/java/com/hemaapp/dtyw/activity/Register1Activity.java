package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.config.DtywConfig;
import com.hemaapp.dtyw.db.SelectDBHelper;
import com.hemaapp.dtyw.model.CityChildren;
import com.hemaapp.dtyw.model.CitySan;
import com.hemaapp.dtyw.model.Select;
import com.hemaapp.dtyw.model.User;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.AreaDialog;
import com.hemaapp.dtyw.view.DtywImageWay;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;

import static com.hemaapp.dtyw.myapplication.R.id;

/**
 * Created by lenovo on 2016/9/18.
 * 注册第二步 填写个人信息
 */
public class Register1Activity extends DtywActivity {
    private ImageButton back_button;//差号
    private TextView title_text;
    private Button next_button;
    private RoundedImageView username_img;
    private LinearLayout nickname_layout;
    private LinearLayout city_layout;
    private TextView username_text;
    private TextView city_text;
    private String username;
    private String password;
    private String type;
    private String tempToken;
    private DtywImageWay imageWay;
    private String imagePathCamera;
    private String tempPath = "";
    private AreaDialog areaDialog;
    private String cityName;
    private SelectDBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.actvity_register1);
        super.onCreate(savedInstanceState);
        helper = SelectDBHelper.get(mContext);
        if (savedInstanceState == null) {
            imageWay = new DtywImageWay(mContext, 1, 2);
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new DtywImageWay(mContext, 1, 2);
        }
        /**
         * 获取全部地址列表
         */
        if (DtywApplication.getInstance().getCityInfo() == null) {
            getNetWorker().addListAllList();
        }
        //获取用户信息
        if (!isNull(type))
        {
            User user = DtywApplication.getInstance().getUser();
            //头像
            if (!isNull(DtywApplication.getInstance().getUser().getAvatar())) {
                username_img.setCornerRadius(100);
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.user_img)
                        .showImageForEmptyUri(R.mipmap.user_img)
                        .showImageOnFail(R.mipmap.user_img).cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoader.getInstance().displayImage(DtywApplication.getInstance().getUser().getAvatar(), username_img, options);

            }
            if (!isNull(DtywApplication.getInstance().getUser().getNickname()))
                username_text.setText(DtywApplication.getInstance().getUser().getNickname());
            if (!isNull(user.getDistrict_name()))
                city_text.setText(user.getDistrict_name());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;

        }
        switch (requestCode) {
            case 1:
                album(data);
                break;
            case 2:
                camera();
                break;
            case 3:
                imageWorker.loadImage(new XtomImageTask(username_img, tempPath,
                        mContext, new XtomImageTask.Size(180, 180)));
                break;
            case 4:
                String nick = data.getStringExtra("context");
                username_text.setText(nick);
                break;
            default:
                break;
        }
    }

    private void album(Intent data) {
        if (data == null)
            return;
        Uri selectedImageUri = data.getData();
        startPhotoZoom(selectedImageUri, 3);
    }

    private void editImage(String path, int requestCode) {
        File file = new File(path);
        startPhotoZoom(Uri.fromFile(file), requestCode);
    }

    private void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", DtywConfig.IMAGE_WIDTH);
        intent.putExtra("aspectY", DtywConfig.IMAGE_WIDTH);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", DtywConfig.IMAGE_WIDTH);
        intent.putExtra("outputY", DtywConfig.IMAGE_WIDTH);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        String savedir = XtomFileUtil.getTempFileDir(mContext);
        File dir = new File(savedir);
        if (!dir.exists())
            dir.mkdirs();
        // 保存入sdCard
        tempPath = savedir + XtomBaseUtil.getFileName() + ".jpg";// 保存路径
        File file = new File(tempPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return file;
    }

    private void camera() {
//		imagePathCamera = null;
//		if (imagePathCamera == null) {
//			imagePathCamera = imageWay.getCameraImage();
//		}
        String path = imageWay.getCameraImage();
        if (!isNull(path))
            imagePathCamera = path;
        log_i("imagePathCamera=" + imagePathCamera);
        editImage(imagePathCamera, 3);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageWay != null)
            outState.putString("imagePathCamera", imageWay.getCameraImage());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDLISTALL_LIST:
                showProgressDialog("获取地区信息");
                break;
            case CLIENT_ADD:
                showProgressDialog("上传个人信息");
                break;
            case FILE_UPLOAD:
                showProgressDialog("上传个人头像");
                break;
            case CLIENT_LOGIN:
                showProgressDialog("登录...");
                break;
            case CLIENT_SAVE:
                showProgressDialog("保存个人信息");
                break;
            case CLIENT_LOGINOUT:
                showProgressDialog("退出当前账号");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDLISTALL_LIST:
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGIN:
            case CLIENT_SAVE:
            case CLIENT_LOGINOUT:
                cancelProgressDialog();
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDLISTALL_LIST:
                HemaArrayResult<CityChildren> result1 = (HemaArrayResult<CityChildren>) hemaBaseResult;
//                CitySan citySan = result1.getObjects().get(0);
//                ArrayList<CitySan> citySanArrayList = ;
                CitySan citySan = new CitySan();
                //     ArrayList<CityChildren> cityChildrens= result1.getObjects();
                citySan.setChildren(result1.getObjects());
                getApplicationContext().setCityInfo(citySan);

                break;
            case CLIENT_ADD:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String token = result.getObjects().get(0);
                if (isNull(tempPath) || tempPath.equals("")) {
                    getNetWorker().clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(password)));
                } else {
                    getNetWorker().fileUpload(token,"1","0","","0","0","无",tempPath);
                }
                break;
            case FILE_UPLOAD:
                if (isNull(type))
                getNetWorker().clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password)));
                else
                {
                    showTextDialog("修改成功");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                }
                break;
            case CLIENT_LOGIN:
                HemaArrayResult<User> result2 = (HemaArrayResult<User>) hemaBaseResult;
                User user = result2.getObjects().get(0);
                  DtywApplication.getInstance().setUser(user);
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password)));
                //添加账号
                Select select = new Select(username,user.getNickname(),password,user.getAvatar());
                helper.insertOrUpdate(select);
                XtomActivityManager.finishAll();
                Intent it1 = new Intent(mContext, MainActivity.class);
                startActivity(it1);
                break;
            case CLIENT_SAVE:
                String token1 = DtywApplication.getInstance().getUser().getToken();
                if (isNull(tempPath) || tempPath.equals("")) {
                  showTextDialog("修改成功");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                } else {
                    getNetWorker().fileUpload(token1,"1","0","","0","0","无",tempPath);
                }
                break;
            case CLIENT_LOGINOUT:
                DtywApplication.getInstance().setUser(null);
                XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
                XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
                log_i("上传的token+++"+tempToken);
                log_i("上传的username+++"+username);
                log_i("上传的password+++"+password);
                getNetWorker().clientAdd(tempToken,username, Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password)),username_text.getText().toString(),city_text.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDLISTALL_LIST:
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGINOUT:
            case CLIENT_LOGIN:
                showTextDialog(hemaBaseResult.getMsg());

                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDLISTALL_LIST:
                showTextDialog("获取地区信息失败，请稍后重试");

                break;
            case CLIENT_ADD:
                showTextDialog("添加个人信息失败，请稍后重试");
                break;
            case FILE_UPLOAD:
                showTextDialog("添加个人头像失败，请稍后重试");
                break;
            case CLIENT_LOGINOUT:
                showTextDialog("退出账号失败，请稍后重试");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(id.back_button);
        title_text = (TextView) findViewById(id.title_text);
        next_button = (Button) findViewById(id.next_button);
        username_text = (TextView) findViewById(id.username_text);
        username_img = (RoundedImageView) findViewById(id.username_img);
        nickname_layout = (LinearLayout) findViewById(id.nickname_layout);
        city_layout = (LinearLayout) findViewById(id.city_layout);
        city_text = (TextView) findViewById(id.city_text);

    }

    @Override
    protected void getExras() {
        username = mIntent.getStringExtra("username");
        password = mIntent.getStringExtra("password");
        type = mIntent.getStringExtra("type");
        tempToken = mIntent.getStringExtra("tempToken");
        log_i("接收到的token++++"+tempToken);
        log_i("接收到的username+++"+username);
        log_i("password+++"+password);
    }

    @Override
    protected void setListener() {
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (isNull(type)) {
            title_text.setText("完善个人资料");
        }
        else
        {
            title_text.setText("编辑个人资料");
        }
        next_button.setText("提交");
        /**
         * 照片
         */
        username_img.setCornerRadius(100);
        username_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                imageWay.show();

            }
        });
        /**
         * 昵称
         */
        nickname_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register1Activity.this, InputWordActivity.class);
                intent.putExtra("type", "nickname");
                intent.putExtra("content", username_text.getText().toString().trim());
                startActivityForResult(intent, 4);
            }
        });
        /**
         * 选择地址
         */
        city_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCity();
            }
        });
        /**
         * 提交
         */
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username_text.getText().toString())) {
                    showTextDialog("请填写昵称");
                    return;
                }
                if (isNull(city_text.getText().toString())) {
                    showTextDialog("请选择地址");
                    return;
                }
                if (isNull(tempPath) && isNull(type))
                {
                    showTextDialog("请选择个人头像");
                    return;
                }

                /**
                 * 注册
                 */
                if (isNull(type))
                {
                    //如果是登录账号切换账号先要退出当前账号
                    User user=DtywApplication.getInstance().getUser();
                    if (user!=null)
                    {
                        String token = user.getToken();
                        getNetWorker().clientLoginout(token);
                        return;
                    }
                    getNetWorker().clientAdd(tempToken,username, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(password)),username_text.getText().toString(),city_text.getText().toString());
                }
                else {
                    //保存
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().clientSave(token,username_text.getText().toString(),city_text.getText().toString());
                }
            }
        });
    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext);
            areaDialog.closeSan();
            areaDialog.setButtonListener(new onbutton());
            return;
        }
        areaDialog.show();
    }

    private class onbutton implements com.hemaapp.dtyw.view.AreaDialog.OnButtonListener {

        @Override
        public void onLeftButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub

            areaDialog.cancel();
        }


        @Override
        public void onRightButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub
            city_text.setText(areaDialog.getTwoText());
            cityName = city_text.getText().toString();
//            homecity = home_text.getText().toString();
//            home_text.setTag(areaDialog.getId());
//            String[] cityid = areaDialog.getId().split(",");
//            homecity = cityid[1];
//            homecounty = cityid[2];
            areaDialog.cancel();
        }

    }
}
