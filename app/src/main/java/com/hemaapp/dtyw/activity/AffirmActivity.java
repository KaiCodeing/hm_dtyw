package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.AffirmAdapter;
import com.hemaapp.dtyw.model.Car;
import com.hemaapp.dtyw.model.Defaultadd;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/9/18.
 * 确认订单
 */
public class AffirmActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    //    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private TextView text_tijiao;
    private Defaultadd defaultadd;
    private ArrayList<Car> cars = new ArrayList<Car>();
    private AffirmAdapter adapter;
    private String type = "0";//1普通发票，2专用发票3没有发票
    private ViewHolder5 holder5;
    private String keyid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_affirm);
        super.onCreate(savedInstanceState);
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fapiao_view, null);
        holder5 = new ViewHolder5();
        findView5(holder5, convertView);
        setData(holder5, 1);
        listview.addFooterView(convertView);
    }
    @Override
    protected void onResume() {
        inIt();
        super.onResume();
    }
    //初始化
    private void inIt() {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().getDefaultadd(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_DEFAULTADD:
                showProgressDialog("获取收货地址");
                break;
            case ORDER_ADD:
                showProgressDialog("添加订单中...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_DEFAULTADD:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                break;
            case ORDER_ADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_DEFAULTADD:
                HemaArrayResult<Defaultadd> result = (HemaArrayResult<Defaultadd>) hemaBaseResult;
                defaultadd = result.getObjects().get(0);
                freshData();
                break;
            case ORDER_ADD:
                HemaArrayResult<String> result1 = (HemaArrayResult<String>) hemaBaseResult;
                 keyid = result1.getObjects().get(0);
                //商品总价
                double spMoney = 0;
                //运费
                double yfNum = 0;
                for (int i = 0; i < cars.size(); i++) {
                    spMoney = ((Double.valueOf(cars.get(i).getBuycount())) * Double.valueOf(cars.get(i).getPrice())) + spMoney;
                    if (!isNull(cars.get(i).getInstallprice()) && !cars.get(i).getInstallid().equals("0"))
                        spMoney = Double.valueOf(cars.get(i).getInstallprice()) + spMoney;
                    if (Double.valueOf(cars.get(i).getShipment()) > yfNum)
                        yfNum = Double.valueOf(cars.get(i).getShipment());
                }

                Intent intent = new Intent(this, Pay1Activity.class);
                intent.putExtra("keyid", keyid);
                intent.putExtra("keytype", "6");
                intent.putExtra("total_fee", String.valueOf(yfNum + spMoney));
                startActivity(intent);
                finish();
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new AffirmAdapter(mContext, cars, this, defaultadd);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setDefaultadd(defaultadd);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_DEFAULTADD:
            case ORDER_ADD:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_DEFAULTADD:
                showTextDialog("获取收货地址失败，请稍后重试");
                break;
            case ORDER_ADD:
                showTextDialog("添加订单失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        //refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        text_tijiao = (TextView) findViewById(R.id.text_tijiao);
    }

    @Override
    protected void getExras() {
        cars = (ArrayList<Car>) mIntent.getSerializableExtra("cars");

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
        title_text.setText("确认订单");
        next_button.setVisibility(View.INVISIBLE);
        //提交
        text_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = DtywApplication.getInstance().getUser().getToken();

                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < cars.size(); i++) {
                    buffer.append(cars.get(i).getId()+",");
                }

                if (checkFP()) {
                    if ("0".equals(type)) {
                        String invoiceheader = holder5.input_taitou.getText().toString().trim();
                        String invoiceitem = holder5.input_content.getText().toString().trim();
                        String invoicedemo = holder5.beizhu_input.getText().toString().trim();
                        getNetWorker().orderAdd(token, buffer.substring(0, buffer.length() - 1), "1", defaultadd.getId(),
                                invoiceheader, invoiceitem, "", "", "", "", "", "", invoicedemo);
                    } else if ("1".equals(type)) {
                        String company = holder5.input_commend.getText().toString().trim();
                        String companyaddress = holder5.input_address_com.getText().toString().trim();
                        String conmpanytel = holder5.input_phone.getText().toString().trim();
                        String bank = holder5.input_kaihu.getText().toString().trim();
                        String banknum = holder5.input_ka_number.getText().toString().trim();
                        String taxnum = holder5.input_shui_number.getText().toString().trim();
                        String invoicedemo = holder5.beizhu_input_2.getText().toString().trim();
                        getNetWorker().orderAdd(token, buffer.substring(0, buffer.length() - 1), "2", defaultadd.getId(),
                                "", "", company, companyaddress, conmpanytel, bank, banknum, taxnum, invoicedemo);
                    } else {
                        getNetWorker().orderAdd(token, buffer.substring(0, buffer.length() - 1), "0", defaultadd.getId(),
                                "", "", "", "", "", "", "", "", "");
                    }
                }
            }
        });
    }

    private boolean checkFP() {
        if (defaultadd.getId().equals("0")) {
            showTextDialog("请填写收货地址");
            return false;
        }
        if ("0".equals(type)) {
            if (isNull(holder5.input_taitou.getText().toString().trim())) {
                showTextDialog("请填写发票抬头");
                return false;
            }
            if (isNull(holder5.input_content.getText().toString().trim())) {
                showTextDialog("请填写发票内容");
                return false;
            }
            if (isNull(holder5.beizhu_input.getText().toString().trim())) {
                showTextDialog("请填写发票备注");
                return false;
            }
        } else if ("1".equals(type)) {
            if (isNull(holder5.input_commend.getText().toString().trim())) {
                showTextDialog("请填写公司名称");
                return false;
            }
            if (isNull(holder5.input_address_com.getText().toString().trim())) {
                showTextDialog("请填写地址");
                return false;
            }
            if (isNull(holder5.input_phone.getText().toString().trim())) {
                showTextDialog("请填写电话号码");
                return false;
            }
            if (isNull(holder5.input_kaihu.getText().toString().trim())) {
                showTextDialog("请填写开户行");
                return false;
            }
            if (isNull(holder5.input_ka_number.getText().toString().trim())) {
                showTextDialog("请填写卡号");
                return false;
            }
            if (isNull(holder5.input_shui_number.getText().toString().trim())) {
                showTextDialog("请填写税号");
                return false;
            }
            if (isNull(holder5.beizhu_input_2.getText().toString().trim())) {
                showTextDialog("请填写备注");
                return false;
            }
        }

        return true;
    }

    /**
     * 发票 item_fapiao_view
     */
    private class ViewHolder5 {
        RadioGroup fapiao_group;
        LinearLayout layout1;
        EditText input_taitou;
        EditText input_content;
        EditText beizhu_input;
        LinearLayout layout2;
        EditText input_commend;
        EditText input_address_com;
        EditText input_phone;
        EditText input_kaihu;
        EditText input_ka_number;
        EditText input_shui_number;
        EditText beizhu_input_2;
    }

    private void findView5(ViewHolder5 holder5, View view) {
        holder5.fapiao_group = (RadioGroup) view.findViewById(R.id.fapiao_group);
        holder5.layout1 = (LinearLayout) view.findViewById(R.id.layout1);
        holder5.input_taitou = (EditText) view.findViewById(R.id.input_taitou);
        holder5.input_content = (EditText) view.findViewById(R.id.input_content);
        holder5.beizhu_input = (EditText) view.findViewById(R.id.beizhu_input);
        holder5.layout2 = (LinearLayout) view.findViewById(R.id.layout2);
        holder5.input_commend = (EditText) view.findViewById(R.id.input_commend);
        holder5.input_address_com = (EditText) view.findViewById(R.id.input_address_com);
        holder5.input_phone = (EditText) view.findViewById(R.id.input_phone);
        holder5.input_kaihu = (EditText) view.findViewById(R.id.input_kaihu);
        holder5.input_ka_number = (EditText) view.findViewById(R.id.input_ka_number);
        holder5.input_shui_number = (EditText) view.findViewById(R.id.input_shui_number);
        holder5.beizhu_input_2 = (EditText) view.findViewById(R.id.beizhu_input_2);

    }

    private void setData(final ViewHolder5 holder5, int position) {
        //选择发票
        holder5.fapiao_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button1:
                        holder5.layout1.setVisibility(View.VISIBLE);
                        holder5.layout2.setVisibility(View.GONE);
                        type = "0";
                        break;
                    case R.id.button2:
                        holder5.layout1.setVisibility(View.GONE);
                        holder5.layout2.setVisibility(View.VISIBLE);
                        type = "1";
                        break;
                    case R.id.button3:
                        holder5.layout1.setVisibility(View.GONE);
                        holder5.layout2.setVisibility(View.GONE);
                        type = "2";
                        break;
                }
            }
        });

    }
}
