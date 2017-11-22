package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.model.SysInitInfo;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import xtom.frame.XtomActivityManager;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.media.XtomVoicePlayer;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/29.
 * 设置
 */
public class SettingActivity extends DtywActivity implements PlatformActionListener{
    private LinearLayout qiehuan_layout;//切换账号
    private LinearLayout zhangh_layout;//账号管理
    private LinearLayout clear_layout;//清除缓存
    private LinearLayout tuisong_layout;//设置推送
    private LinearLayout yijian_layout;//意见反馈
    private LinearLayout share_layout;//软件分享
    private LinearLayout about_we_layout;//关于我们
    private TextView out_app;//退出登录
    private ImageView check_tuisong;
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private String pushType="0";
    private ViewHolder holder;
    private ShareHolder shareHolder;
    private OnekeyShare oks;
    private String sys_plugins;
    private String pathWX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
        inIt();
        ShareSDK.initSDK(this);
        SysInitInfo initInfo = getApplicationContext()
                .getSysInitInfo();
        sys_plugins = initInfo.getSys_plugins();
        pathWX = sys_plugins + "share/sdk.php?id=0&keytype=2";
        log_i("++"+pathWX);
    }
    private void inIt()
    {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().notice(token);
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
       DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GETNOTICE:
                showProgressDialog("保存推送操作");
                break;
            case NOTICE:

                break;
            case CLIENT_LOGINOUT:
                showProgressDialog("退出登录");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GETNOTICE:
            case CLIENT_LOGINOUT:
                cancelProgressDialog();
                break;
            case NOTICE:

                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GETNOTICE:
                inIt();
                break;
            case NOTICE:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                pushType = result.getObjects().get(0);
                if ("0".equals(pushType))
                    check_tuisong.setImageResource(R.mipmap.check_tuisong_on);
                else
                check_tuisong.setImageResource(R.mipmap.check_tuisong_off);
                break;
            case CLIENT_LOGINOUT:
                DtywApplication.getInstance().setUser(null);
                XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
                XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
                //XtomSharedPreferencesUtil.save(getActivity(), "city_name", "");
                XtomActivityManager.finishAll();
                Intent it = new Intent(mContext, LoginActivity.class);
                startActivity(it);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GETNOTICE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case NOTICE:
            case CLIENT_LOGINOUT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GETNOTICE:
                showTextDialog("操作失败，请稍后重试");
                break;
            case NOTICE:
                showTextDialog("获取推送状态失败，请稍后重试");
                check_tuisong.setEnabled(false);
                break;
            case CLIENT_LOGINOUT:
                showTextDialog("退出登录失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        qiehuan_layout = (LinearLayout) findViewById(R.id.qiehuan_layout);
        zhangh_layout = (LinearLayout) findViewById(R.id.zhangh_layout);
        clear_layout = (LinearLayout) findViewById(R.id.clear_layout);
        tuisong_layout = (LinearLayout) findViewById(R.id.tuisong_layout);
        yijian_layout = (LinearLayout) findViewById(R.id.yijian_layout);
        share_layout = (LinearLayout) findViewById(R.id.share_layout);
        about_we_layout = (LinearLayout) findViewById(R.id.about_we_layout);
        out_app = (TextView) findViewById(R.id.out_app);
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        check_tuisong = (ImageView) findViewById(R.id.check_tuisong);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        title_text.setText("设置");
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //切换账号
        qiehuan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ChangeUserActivity.class);
                startActivity(intent);
            }
        });
        //zhangh_layout账号管理
        zhangh_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,UserManageActivity.class);
                startActivity(intent);
            }
        });
        //清除缓存
        clear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearTask().execute();
            }
        });
        check_tuisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = DtywApplication.getInstance().getUser().getToken();
                if ("1".equals(pushType))
                    getNetWorker().getnotice(token,"0");
                else
                    getNetWorker().getnotice(token,"1");
            }
        });
        //意见反馈
        yijian_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,SuggestionActivity.class);
                startActivity(intent);
            }
        });
        //软件分享
        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelect();
            }
        });
        //关于我们
        about_we_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(SettingActivity.this,WebviewActivity.class);
                intent.putExtra("type","3");
                startActivity(intent);
            }
        });
        //退出登录
        out_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOut();
            }
        });

    }
    //分享
    private class ShareHolder{
        TextView textView1_camera;
        TextView radiobutton3;
        TextView radiobutton1;
        TextView radiobutton2;
        TextView radiobutton0;
    }
    private void showSelect() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwind_share_view, null);
        shareHolder = new ShareHolder();
        shareHolder.textView1_camera = (TextView) view.findViewById(R.id.textView1_camera);
        shareHolder.radiobutton3 = (TextView) view.findViewById(R.id.radiobutton3);
        shareHolder.radiobutton1 = (TextView) view.findViewById(R.id.radiobutton1);
        shareHolder.radiobutton2 = (TextView) view.findViewById(R.id.radiobutton2);
        shareHolder.radiobutton0 = (TextView) view.findViewById(R.id.radiobutton0);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        shareHolder.textView1_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        shareHolder.radiobutton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(QQ.NAME);
                popupWindow.dismiss();
            }
        });
        shareHolder.radiobutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(Wechat.NAME);
                popupWindow.dismiss();
            }
        });

        shareHolder.radiobutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(QZone.NAME);
                popupWindow.dismiss();
            }
        });
        shareHolder.radiobutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(SinaWeibo.NAME);
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //   popupWindow.showAsDropDown(findViewById(R.id.pop_layout_bottom));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
    private void showShare(String platform) {
        if (oks == null) {
            oks = new OnekeyShare();
          //  oks.setTitleUrl(pathWX);
//            oks.setTitle("梯配网");
//            oks.setText("推荐梯配网");
//            oks.setImageUrl("");
//           oks.setFilePath(pathWX);
//            oks.setImagePath("");
//            oks.setUrl(pathWX);
//          //  oks.setSiteUrl(pathWX);
//            oks.setCallback(this);

            oks.setTitleUrl(pathWX); // 标题的超链接
            oks.setTitle("梯配网");
             oks.setText("专业的电梯配件APP");
            oks.setImageUrl("");
            oks.setFilePath(pathWX);

           // oks.setImageUrl(getLogoImagePath());
            oks.setImagePath(getLogoImagePath());
            oks.setUrl(pathWX);
            oks.setSiteUrl(pathWX);
            oks.setCallback(this);

        }
        oks.setPlatform(platform);
        oks.show(mContext);
    }
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是微信
            handler.sendEmptyMessage(1);
        }
        if (platform.getName().equals(Wechat.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(2);
        }
        if (platform.getName().equals(QZone.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(3);
        }
        if (platform.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(4);
        }
    }
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "空间分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "分享失败" , Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 6;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);

    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(5);
    }


    private class ViewHolder{
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }
    private void showOut()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_right_left, null);
        holder = new ViewHolder();
        holder.close_pop = (TextView) view.findViewById(R.id.close_pop);
        holder.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        holder.text = (TextView) view.findViewById(R.id.text);
        holder.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        holder.text.setText("确定要退出软件吗?");
        holder.iphone_number.setVisibility(View.GONE);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        holder.close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        holder.yas_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().clientLoginout(token);
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }
    private class ClearTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // 删除图片缓存
            XtomImageCache.getInstance(mContext).deleteCache();
            // 删除语音缓存
            XtomVoicePlayer player = XtomVoicePlayer.getInstance(mContext);
            player.deleteCache();
            player.release();
            return null;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在清除缓存");
        }

        @Override
        protected void onPostExecute(Void result) {
            cancelProgressDialog();
            showTextDialog("缓存清理完毕！");
          //  cache_number.setText(bytes2kb(XtomImageCache.getInstance(mContext).getCacheSize()));
        }

    }
    // 获取软件Logo文件地址
    private String getLogoImagePath() {
        String imagePath;
        try {
            String cachePath_internal = XtomFileUtil.getCacheDir(mContext)
                    + "/images/";// 获取缓存路径
            File dirFile = new File(cachePath_internal);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            imagePath = cachePath_internal + "ic_lauer.png";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(
                        mContext.getResources(), R.mipmap.ic_launcher);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            imagePath = null;
        }
        log_i("imagePath=" + imagePath);
        return imagePath;
    }

}
