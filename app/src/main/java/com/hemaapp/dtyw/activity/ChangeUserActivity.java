package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.db.SelectDBHelper;
import com.hemaapp.dtyw.model.Select;
import com.hemaapp.dtyw.model.User;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/29.
 * 切换账号
 * ADDVIEW add_view_user
 */
public class ChangeUserActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private LinearLayout add_view;
    private LinearLayout add_address_layout;
    private ArrayList<Select> selects = new ArrayList<Select>();
    private String type = "0";//0没有删除 1 编辑删除
    private SelectDBHelper helper;
    private String login_ty = "0";
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_change);
        super.onCreate(savedInstanceState);
        helper = SelectDBHelper.get(mContext);
    }

    @Override
    protected void onResume() {
        getHistoryList();
        super.onResume();
    }

    private void inIt() {
        add_view.removeAllViews();
        if (selects==null)
        {
            return;
        }
        for (int i = 0; i < selects.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.add_view_user, null);
            ImageView delete_user_ = (ImageView) view.findViewById(R.id.delete_user_);
            TextView user_nick_name = (TextView) view.findViewById(R.id.user_nick_name);
            ImageView selete_user = (ImageView) view.findViewById(R.id.selete_user);
            RoundedImageView user_img = (RoundedImageView) view.findViewById(R.id.user_img);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.user_img)
                    .showImageForEmptyUri(R.mipmap.user_img)
                    .showImageOnFail(R.mipmap.user_img).cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoader.getInstance().displayImage(selects.get(i).getAvatar(), user_img, options);
            if ("0".equals(type))
                delete_user_.setVisibility(View.GONE);
            else
                delete_user_.setVisibility(View.VISIBLE);
            user_nick_name.setText(selects.get(i).getNickname());
            if (selects.get(i).getUsername().equals(XtomSharedPreferencesUtil.get(mContext, "username")))
                selete_user.setImageResource(R.mipmap.check_anzhuang_off);
            else
                selete_user.setImageResource(R.mipmap.check_anzhuang_on);
            selete_user.setTag(R.id.TAG, selects.get(i));
            //切换账号
            selete_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Select select = (Select) v.getTag(R.id.TAG);
                    if (select.getUsername().equals(XtomSharedPreferencesUtil.get(mContext, "username"))) {
                    } else {
                        //退出登录
                        username = select.getUsername();
                        password = select.getPassword();
                        login_ty = "0";
                        String token = DtywApplication.getInstance().getUser().getToken();
                        getNetWorker().clientLoginout(token);

                    }
                }
            });
            delete_user_.setTag(R.id.TAG, selects.get(i));
            delete_user_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Select select = (Select) v.getTag(R.id.TAG);
                    if (select.getUsername().equals(XtomSharedPreferencesUtil.get(mContext, "username"))) {
                        login_ty = "1";
                        String token = DtywApplication.getInstance().getUser().getToken();
                        getNetWorker().clientLoginout(token);
                    }

                    helper.delete(select);
                    getHistoryList();
                }

            });
            add_view.addView(view);
        }
    }

    private void getHistoryList() {
        new LoadDBTask().execute();
    }

    private class LoadDBTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // XtomProcessDialog.show(mContext, R.string.loading);
        }

        @Override
        protected Void doInBackground(Void... params) {
            selects = helper.select();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            //   getHistoryList_done();
            inIt();
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                showTextDialog("登录账号中...");
                break;
            case CLIENT_LOGINOUT:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
            case CLIENT_LOGINOUT:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                HemaArrayResult<User> userArrayResult = (HemaArrayResult<User>) hemaBaseResult;
                User user = userArrayResult.getObjects().get(0);
                getApplicationContext().setUser(user);
                String username = hemaNetTask.getParams().get("username");
                String password = hemaNetTask.getParams().get("password");
                //添加账号
                Select select = new Select(username, user.getNickname(), user.getPassword(), user.getAvatar());
                helper.insertOrUpdate(select);
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", password);
                XtomActivityManager.finishAll();
                String token = user.getToken();
                Intent intent = new Intent(ChangeUserActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case CLIENT_LOGINOUT:
                DtywApplication.getInstance().setUser(null);
                XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
                XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
                //XtomSharedPreferencesUtil.save(getActivity(), "city_name", "");
                if (login_ty.equals("0")) {
                    getNetWorker().clientLogin(this.username,
                            this.password);
                } else {
                    XtomActivityManager.finishAll();
                    Intent it = new Intent(mContext, LoginActivity.class);
                    it.putExtra("log", "log");
                    startActivity(it);
                }

                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
            case CLIENT_LOGINOUT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                showTextDialog("登录失败，请稍后重试");
                break;
            case CLIENT_LOGINOUT:
                showTextDialog("退出登录失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        add_view = (LinearLayout) findViewById(R.id.add_view);
        add_address_layout = (LinearLayout) findViewById(R.id.add_address_layout);
    }

    @Override
    protected void getExras() {

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
        title_text.setText("切换账号");
        next_button.setText("编辑");
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("0")) {
                    type = "1";
                    add_address_layout.setVisibility(View.INVISIBLE);
                } else {
                    type = "0";
                    add_address_layout.setVisibility(View.VISIBLE);
                }
                getHistoryList();
            }
        });
        add_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeUserActivity.this, LoginActivity.class);
                intent.putExtra("log", "log");
                startActivity(intent);
            }
        });
    }
}
