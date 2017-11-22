package com.hemaapp.dtyw.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywFragment;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.activity.LoginActivity;
import com.hemaapp.dtyw.activity.MyKeepActivity;
import com.hemaapp.dtyw.activity.MyOrderActivity;
import com.hemaapp.dtyw.activity.MyWalletActivity;
import com.hemaapp.dtyw.activity.RefundmentListActivity;
import com.hemaapp.dtyw.activity.Register1Activity;
import com.hemaapp.dtyw.activity.RequsetListActivity;
import com.hemaapp.dtyw.activity.SelectAddressActivity;
import com.hemaapp.dtyw.activity.SettingActivity;
import com.hemaapp.dtyw.activity.WebviewActivity;
import com.hemaapp.dtyw.model.OrderNum;
import com.hemaapp.dtyw.model.User;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/29.
 * 个人中心
 */
public class MyFragment extends DtywFragment {
    private RoundedImageView user_img;
    private ImageView setting_img;
    private TextView user_name_text;
    private LinearLayout my_dingdan_layout;
    private TextView daifukuan;
    private TextView daifahuo;
    private TextView daishouhuo;
    private TextView daipingjia;
    private TextView tuikuan;
    private LinearLayout money_layout;//我的钱包
    private LinearLayout shoucang_layout;//我的收藏
    private LinearLayout help_layout;//帮助
    private LinearLayout address_layout;//地址管理
    private String username;
    private TextView money_number_text;
    private TextView dfk_number;
    private TextView dfh_number;
    private TextView dsh_number;
    private TextView dpj_number;
    private LinearLayout issue_layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_my);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        username = XtomSharedPreferencesUtil.get(getActivity(), "username");
        if (!isNull(username)) {
            String token = DtywApplication.getInstance().getUser().getToken();
            getNetWorker().getFeeaccount(token);
        }
        else
        {
            user_img.setImageResource(R.mipmap.user_img);
            user_name_text.setText("登录/注册");
            money_number_text.setText("0.0");
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
                break;
            case CLIENT_LOGIN:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
                break;
            case CLIENT_LOGIN:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String money = result.getObjects().get(0);
                money_number_text.setText(money);
                String username = XtomSharedPreferencesUtil.get(getActivity(), "username");
                String password = XtomSharedPreferencesUtil.get(getActivity(), "password");
                getNetWorker().clientLogin(username, password);
                break;
            case CLIENT_LOGIN:
                HemaArrayResult<User> result2 = (HemaArrayResult<User>) hemaBaseResult;
                User user = result2.getObjects().get(0);
                DtywApplication.getInstance().setUser(user);
                if (!isNull(DtywApplication.getInstance().getUser().getAvatar())) {
                    user_img.setCornerRadius(100);
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.user_img)
                            .showImageForEmptyUri(R.mipmap.user_img)
                            .showImageOnFail(R.mipmap.user_img).cacheInMemory(true)
                            .bitmapConfig(Bitmap.Config.RGB_565).build();
                    ImageLoader.getInstance().displayImage(DtywApplication.getInstance().getUser().getAvatar(), user_img, options);

                }
                if (!isNull(DtywApplication.getInstance().getUser().getNickname()))
                    user_name_text.setText(DtywApplication.getInstance().getUser().getNickname());
                //订单数量
                getNetWorker().orderNum(user.getToken());

                break;
            case ORDER_NUM:
                HemaArrayResult<OrderNum> result3 = (HemaArrayResult<OrderNum>) hemaBaseResult;
                OrderNum orderNum = result3.getObjects().get(0);
                //待付款
                if (isNull(orderNum.getDfknum()) || "0".equals(orderNum.getDfknum()))
                    dfk_number.setVisibility(View.INVISIBLE);
                else
                {
                    dfk_number.setVisibility(View.VISIBLE);
                    dfk_number.setText(orderNum.getDfknum());
                }
                //待发货
                if (isNull(orderNum.getDfhnum()) || "0".equals(orderNum.getDfhnum()))
                    dfh_number.setVisibility(View.INVISIBLE);
                else
                {
                    dfh_number.setVisibility(View.VISIBLE);
                    dfh_number.setText(orderNum.getDfhnum());
                }
                //待收货
                if (isNull(orderNum.getDshnum()) || "0".equals(orderNum.getDshnum()))
                    dsh_number.setVisibility(View.INVISIBLE);
                else
                {
                    dsh_number.setVisibility(View.VISIBLE);
                    dsh_number.setText(orderNum.getDshnum());
                }
                //待评价
                if (isNull(orderNum.getDpjnum()) || "0".equals(orderNum.getDpjnum()))
                    dpj_number.setVisibility(View.INVISIBLE);
                else
                {
                    dpj_number.setVisibility(View.VISIBLE);
                    dpj_number.setText(orderNum.getDpjnum());
                }
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case CLIENT_LOGIN:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
                showTextDialog("获取余额失败，请稍后重试");
                break;
            case CLIENT_LOGIN:
                showTextDialog("获取个人信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        user_img = (RoundedImageView) findViewById(R.id.user_img);
        setting_img = (ImageView) findViewById(R.id.setting_img);
        user_name_text = (TextView) findViewById(R.id.user_name_text);
        my_dingdan_layout = (LinearLayout) findViewById(R.id.my_dingdan_layout);
        daifukuan = (TextView) findViewById(R.id.daifukuan);
        daifahuo = (TextView) findViewById(R.id.daifahuo);
        daishouhuo = (TextView) findViewById(R.id.daishouhuo);
        daipingjia = (TextView) findViewById(R.id.daipingjia);
        tuikuan = (TextView) findViewById(R.id.tuikuan);
        money_layout = (LinearLayout) findViewById(R.id.money_layout);
        shoucang_layout = (LinearLayout) findViewById(R.id.shoucang_layout);
        help_layout = (LinearLayout) findViewById(R.id.help_layout);
        address_layout = (LinearLayout) findViewById(R.id.address_layout);
        money_number_text = (TextView) findViewById(R.id.money_number_text);

        dfk_number = (TextView) findViewById(R.id.dfk_number);
        dfh_number = (TextView) findViewById(R.id.dfh_number);
        dsh_number = (TextView) findViewById(R.id.dsh_number);
        dpj_number = (TextView) findViewById(R.id.dpj_number);
        issue_layout = (LinearLayout) findViewById(R.id.issue_layout);

    }

    //跳转登录
    private void goLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    protected void setListener() {
        username = XtomSharedPreferencesUtil.get(getActivity(), "username");
        if (isNull(username))
            money_number_text.setVisibility(View.INVISIBLE);
        else {
            money_number_text.setVisibility(View.VISIBLE);

        }
        //头像跳转
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), Register1Activity.class);
                    intent.putExtra("type", "1");
                    startActivity(intent);
                }
            }
        });
        //设置跳转
        setting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
        //全部订单跳转
        my_dingdan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                    intent.putExtra("type","0");
                    getActivity().startActivity(intent);
                }
            }
        });
        //代付款
        daifukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                    intent.putExtra("type","1");
                    getActivity().startActivity(intent);
                }
            }
        });
        //待发货
        daifahuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                    intent.putExtra("type","2");
                    getActivity().startActivity(intent);
                }
            }
        });
        //待收货
        daishouhuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                    intent.putExtra("type","3");
                    getActivity().startActivity(intent);
                }
            }
        });
        //待评价
        daipingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                    intent.putExtra("type","4");
                    getActivity().startActivity(intent);
                }
            }
        });
        //tuikuan退款
        tuikuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), RefundmentListActivity.class);
                    startActivity(intent);
                }
            }
        });
        //我的钱包
        money_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), MyWalletActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
        //我的收藏
        shoucang_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else
                {
                    Intent intent = new Intent(getActivity(), MyKeepActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
        //帮助中心
        help_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else
                {
                    Intent intent = new Intent(getActivity(), WebviewActivity.class);
                    intent.putExtra("type","4");
                    getActivity().startActivity(intent);
                }
            }
        });
        //地址管理
        address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else
                {
                    Intent intent = new Intent(getActivity(), SelectAddressActivity.class);
                    intent.putExtra("type","1");
                    getActivity().startActivity(intent);
                }
            }
        });
        issue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(username))
                    goLogin();
                else {
                    Intent intent = new Intent(getActivity(), RequsetListActivity.class);
                    intent.putExtra("keytype", "2");
                    getActivity().startActivity(intent);
                }
            }
        });
    }
}
