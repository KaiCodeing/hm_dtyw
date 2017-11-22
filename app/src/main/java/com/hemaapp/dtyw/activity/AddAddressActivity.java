package com.hemaapp.dtyw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.model.CityChildren;
import com.hemaapp.dtyw.model.CitySan;
import com.hemaapp.dtyw.model.Shipaddress;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.AreaDialog;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

/**
 * Created by lenovo on 2016/9/22.
 * /添加地址
 */
public class AddAddressActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private EditText user_name_text;
    private EditText user_phone_text;
    private LinearLayout address_layout;
    private EditText user_sh_address_text;
    private Shipaddress shipaddress;
    private TextView user_address_text;
    private AreaDialog areaDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_address);
        super.onCreate(savedInstanceState);
        inIt();
        /**
         * 获取全部地址列表
         */
        if (DtywApplication.getInstance().getCityInfo() == null) {
            getNetWorker().addListAllList();
        }
    }

    private void inIt() {
        if (shipaddress != null) {
            //名字
            if (!isNull(shipaddress.getClientname()))
                user_name_text.setText(shipaddress.getClientname());
            //地址
            if (!isNull(shipaddress.getAddress()))
                user_sh_address_text.setText(shipaddress.getLocation());
            //电话
            if (!isNull(shipaddress.getTel()))
                user_phone_text.setText(shipaddress.getTel());
            //所在城市
            if (!isNull(shipaddress.getCity()))
                user_address_text.setText(shipaddress.getCity());
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDLISTALL_LIST:
                showProgressDialog("获取地区信息");
                break;
            case SHIPADDRESS_ADD:
                showProgressDialog("添加收货地址...");
                break;
            case SHIPADDRESS_SAVE:
                showTextDialog("修改收货地址...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDLISTALL_LIST:
            case SHIPADDRESS_ADD:
            case SHIPADDRESS_SAVE:
                cancelProgressDialog();
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
            case SHIPADDRESS_ADD:
                showTextDialog("添加地址成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case SHIPADDRESS_SAVE:
                showTextDialog("修改地址成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDLISTALL_LIST:
            case SHIPADDRESS_ADD:
            case SHIPADDRESS_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
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
            case SHIPADDRESS_ADD:
                showTextDialog("添加收货地址失败，请稍后重试");
                break;
            case SHIPADDRESS_SAVE:
                showTextDialog("修改收货地址失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        user_name_text = (EditText) findViewById(R.id.user_name_text);
        user_phone_text = (EditText) findViewById(R.id.user_phone_text);
        address_layout = (LinearLayout) findViewById(R.id.address_layout);
        user_sh_address_text = (EditText) findViewById(R.id.user_sh_address_text);
        user_address_text = (TextView) findViewById(R.id.user_address_text);
    }

    @Override
    protected void getExras() {
        shipaddress = (Shipaddress) mIntent.getSerializableExtra("address");
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
        if (shipaddress == null) {
            title_text.setText("添加地址");
        } else {
            title_text.setText("地址编辑");
        }
        next_button.setText("保存");
        //获取地址
        address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCity();
            }
        });
        //保存或添加
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加新的地址
                String uaername = user_name_text.getText().toString().trim();
                String tel = user_phone_text.getText().toString().trim();
                String city = user_address_text.getText().toString().trim();
                String loaction = user_sh_address_text.getText().toString().trim();
                String token = DtywApplication.getInstance().getUser().getToken();
                if (isNull(uaername)) {
                    showTextDialog("请填写联系人姓名");
                    return;
                }
                if (isNull(tel)) {
                    showTextDialog("请填写收货电话");
                    return;
                }
                String mobile = "\\d{11}";// 只判断11位
                if (!tel.matches(mobile)) {
                    showTextDialog("手机格式不正确，请重新输入");
                    return;
                }
                if (isNull(city)) {
                    showTextDialog("请选择收货城市");
                    return;
                }
                if (isNull(loaction)) {
                    showTextDialog("请选择收货地址");
                    return;
                }
                if (shipaddress == null) {
                    getNetWorker().shipaddressAdd(token, uaername, tel, city, loaction, "0");
                } else {
                    if ("0".equals(shipaddress.getDefaultadd()))
                        getNetWorker().shipaddressSave(token, shipaddress.getId(), uaername, tel, city, loaction, "0");
                    else
                        getNetWorker().shipaddressSave(token, shipaddress.getId(), uaername, tel, city, loaction, "1");
                }
            }
        });

    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext);

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
            user_address_text.setText(areaDialog.getText());
            //  cityName = city_text.getText().toString();
//            homecity = home_text.getText().toString();
//            home_text.setTag(areaDialog.getId());
//            String[] cityid = areaDialog.getId().split(",");
//            homecity = cityid[1];
//            homecounty = cityid[2];
            areaDialog.cancel();
        }

    }
}
