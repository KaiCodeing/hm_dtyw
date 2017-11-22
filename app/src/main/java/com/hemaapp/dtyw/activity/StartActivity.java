package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetWorker;
import com.hemaapp.dtyw.model.SysInitInfo;
import com.hemaapp.dtyw.model.User;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.net.MalformedURLException;
import java.net.URL;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/13.
 * 启动页
 */
public class StartActivity extends DtywActivity {

    private ImageView imageView;
    private SysInitInfo sysInitInfo;
    private User user;
    private String fromNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_start);
        super.onCreate(savedInstanceState);
        sysInitInfo = getApplicationContext().getSysInitInfo();
        user = getApplicationContext().getUser();
     //   DtywLocation.getInstance().startLocation();

        init();
    }

    private void init() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo);
        animation.setAnimationListener(new StartAnimationListener());
        imageView.startAnimation(animation);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        // TODO Auto-generated method stub
        DtywHttpInformation information = (DtywHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                HemaArrayResult<SysInitInfo> sResult = (HemaArrayResult<SysInitInfo>) baseResult;
                sysInitInfo = sResult.getObjects().get(0);
                //setStartImage();
                getApplicationContext().setSysInitInfo(sysInitInfo);
                String adsshow = XtomSharedPreferencesUtil.get(mContext, "adsshow");
                if (isNull(adsshow)) {
                    toAds();
                    finish();
                }
                else {
                    checkLogin();
                }


                // toAds();
                break;
            case CLIENT_LOGIN:
                HemaArrayResult<User> uResult = (HemaArrayResult<User>) baseResult;
                user = uResult.getObjects().get(0);
                getApplicationContext().setUser(user);
                toMain();
                break;
            default:
                break;
        }
    }

    /**
     *
     * @方法名称: toAds
     * @功能描述: TODO跳转到广告页
     * @返回值: void
     */
    private void toAds() {
        Intent intent = new Intent(StartActivity.this, AdsActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @方法名称: setStartImage
     * @功能描述: 获取启动图片地址，并显示
     * @返回值: void
     */
    private void setStartImage() {
        String StartImage = null;
        if (sysInitInfo != null) {
            StartImage = sysInitInfo.getStart_img();
        }
        if (!isNull(StartImage)) {
            URL url;
            try {
                url = new URL(StartImage);
                imageWorker.loadImage(new ImageTask(imageView, url, mContext));
            } catch (MalformedURLException e) {

                imageView.setImageResource(R.mipmap.startimg);
            }
        }
    }

    private class ImageTask extends XtomImageTask {

        public ImageTask(ImageView imageView, URL url, Object context) {
            super(imageView, url, context);
        }

        @Override
        public void beforeload() {
            imageView.setImageResource(R.mipmap.startimg);
        }

        @Override
        public void failed() {
            log_w("Get image " + path + " failed!!!");
            imageView.setImageResource(R.mipmap.startimg);
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        // TODO Auto-generated method stub
        DtywHttpInformation information = (DtywHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                getInitFailed();
                break;
            case CLIENT_LOGIN:
                int key = baseResult.getError_code();
                if (key==102 || key==104 || key==106) {
                    DtywApplication.getInstance().setUser(null);
                    XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
                    XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
                }
                toMain();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub
        DtywHttpInformation information = (DtywHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                getInitFailed();
                break;
            case CLIENT_LOGIN:
                DtywApplication.getInstance().setUser(null);
                XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
                XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
                toMain();
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        // TODO Auto-generated method stub
        imageView = (ImageView) findViewById(R.id.imageview);
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub
        fromNotification = mIntent.getStringExtra("fromNotification");
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }

    /**
     *
     * @方法名称: getInitFailed
     * @功能描述: 初始化失败提示
     * @返回值: void
     */
    private void getInitFailed() {
        if (sysInitInfo != null) {
            checkLogin();
        } else {
            showTextDialog("获取系统初始化信息失败啦\n请检查网络连接重试");
        }
    }

    private void checkLogin() {
        if (isAutoLogin()) {
            String username = XtomSharedPreferencesUtil.get(this, "username");
            String password = XtomSharedPreferencesUtil.get(this, "password");
            if (!isNull(username) && !isNull(password)) {
                DtywNetWorker netWorker = getNetWorker();
                netWorker.clientLogin(username, password);
            } else {
                toMain();
            }
        } else {
            toMain();
        }

    }

    /**
     *
     * @方法名称: isAutoLogin
     * @功能描述: 检测是否是自动登录
     * @return
     * @返回值: boolean
     */
    private boolean isAutoLogin() {
        String autologin = XtomSharedPreferencesUtil.get(mContext, "autoLogin");
        boolean no = "no".equals(autologin);
        return !no;
    }

    /**
     *
     * @方法名称: toLogin
     * @功能描述: 跳转到登录界面
     * @返回值: void
     */
    private void toLogin() {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     *
     * @方法名称: toMain
     * @功能描述: 跳转到主界面
     * @返回值: void
     */
    private void toMain() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra("fromNotification", fromNotification);
        log_i("启动页的信息是什么-----------"+fromNotification);
        startActivity(intent);
        finish();

    }

    private class StartAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            DtywNetWorker netWorker = getNetWorker();
            netWorker.inIt();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }
    }


}
