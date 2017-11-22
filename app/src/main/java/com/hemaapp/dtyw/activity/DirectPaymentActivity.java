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
import com.hemaapp.dtyw.adapter.DirectPaymentAdapter;
import com.hemaapp.dtyw.model.Defaultadd;
import com.hemaapp.dtyw.model.GoodsGet;
import com.hemaapp.dtyw.model.Installservice;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/10/29.
 */
public class DirectPaymentActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    //    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private TextView text_tijiao;
    private GoodsGet goodsGet;
    private ArrayList<Installservice> installservices = new ArrayList<Installservice>();
    private String price;
    private String type="0";
    private ViewHolder5 holder5;
    private String keyid;
    private Defaultadd defaultadd;
    private String goodId;
    private String valueId;
    private String anid;
    private String anvalue;
    private DirectPaymentAdapter adapter;
    private String peopery;
    private String stock;
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
        super.onResume();
        inIt();
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
            case GOODS_BUY:

                break;
        }
    }
    private void freshData() {
        if (adapter == null) {
            adapter = new DirectPaymentAdapter(mContext, goodsGet, installservices, this,defaultadd,price,anvalue,peopery,stock);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setDefaultadd(defaultadd);
            adapter.setGoodsGet(goodsGet);
            adapter.setPrice(price);

            adapter.setPeopery(peopery);
            adapter.notifyDataSetChanged();

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
            case GOODS_BUY:
                HemaArrayResult<String> result1 = (HemaArrayResult<String>) hemaBaseResult;
                keyid = result1.getObjects().get(0);

                Intent intent = new Intent(this, Pay1Activity.class);
                intent.putExtra("keyid", keyid);
                intent.putExtra("keytype", "6");
                intent.putExtra("total_fee",String.valueOf((Double.valueOf(goodsGet.getNumber())*Double.valueOf(price))+Double.valueOf(anvalue)+Double.valueOf(goodsGet.getShipment())));
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_DEFAULTADD:
            case GOODS_BUY:
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
            case GOODS_BUY:
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
        goodsGet = (GoodsGet) mIntent.getSerializableExtra("good");
        installservices = (ArrayList<Installservice>) mIntent.getSerializableExtra("installservice");
        price = mIntent.getStringExtra("price");
        valueId = mIntent.getStringExtra("valueId");
        peopery = mIntent.getStringExtra("peopery") ;
        goodId = mIntent.getStringExtra("goodId");
        anid = mIntent.getStringExtra("anid");
        anvalue = mIntent.getStringExtra("anvalue");
        stock = mIntent.getStringExtra("stock");
        if (anid.equals("0"))
        {  anvalue="0";
            anid="0";}
    }

    @Override
    protected void setListener() {
    next_button.setVisibility(View.INVISIBLE);
        title_text.setText("确认订单");
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //提交
        text_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = DtywApplication.getInstance().getUser().getToken();
                if (checkFP()) {
                    if ("0".equals(type)) {
                        String invoiceheader = holder5.input_taitou.getText().toString().trim();
                        String invoiceitem = holder5.input_content.getText().toString().trim();
                        String invoicedemo = holder5.beizhu_input.getText().toString().trim();
                        getNetWorker().goodBuy(token,goodId, goodsGet.getNumber(), anid,
                                valueId, defaultadd.getId(), "1", invoiceheader, invoiceitem, "", "", "","","","",invoicedemo);
                    } else if ("1".equals(type)) {
                        String company = holder5.input_commend.getText().toString().trim();
                        String companyaddress = holder5.input_address_com.getText().toString().trim();
                        String conmpanytel = holder5.input_phone.getText().toString().trim();
                        String bank = holder5.input_kaihu.getText().toString().trim();
                        String banknum = holder5.input_ka_number.getText().toString().trim();
                        String taxnum = holder5.input_shui_number.getText().toString().trim();
                        String invoicedemo = holder5.beizhu_input_2.getText().toString().trim();
                        getNetWorker().goodBuy(token,goodId, goodsGet.getNumber(), anid,
                                valueId, defaultadd.getId(), "2", "","", company,companyaddress,conmpanytel,bank,banknum,taxnum,invoicedemo);
                    } else {
                        getNetWorker().goodBuy(token,goodId, goodsGet.getNumber(), anid,
                                valueId, defaultadd.getId(), "0", "","", "","","","","","","");
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
