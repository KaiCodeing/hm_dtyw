package com.hemaapp.dtyw.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.model.RefundDetails;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

/**
 * Created by lenovo on 2016/10/9.
 * 退款详情
 */
public class RefundmentActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView tui_outcome;
    private TextView outcome_text;
    private TextView reason_text;
    private TextView infor_text;
    private TextView pop_show;
    private String orderid;//退款订单id
    private String cartid;//购物车id
    private ViewHolder holder;
    private String goodsid;//商品id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refundment);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt() {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().refundDetails(token, orderid, cartid,goodsid);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REFUND_DETAILS:
                showProgressDialog("获取退款订单信息");
                break;
            case DELIVERY_SAVE:
                showProgressDialog("提交快递信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REFUND_DETAILS:
                cancelProgressDialog();
                break;
            case DELIVERY_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REFUND_DETAILS:
                HemaArrayResult<RefundDetails> result = (HemaArrayResult<RefundDetails>) hemaBaseResult;
                RefundDetails details = result.getObjects().get(0);
                setData(details);
                break;
            case DELIVERY_SAVE:
                showTextDialog("快递信息提交成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },1000);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REFUND_DETAILS:
            case DELIVERY_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REFUND_DETAILS:
                showTextDialog("获取退款订单信息失败，请稍后重试");
                break;
            case DELIVERY_SAVE:
                showTextDialog("快递信息提交失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        tui_outcome = (TextView) findViewById(R.id.tui_outcome);
        outcome_text = (TextView) findViewById(R.id.outcome_text);
        reason_text = (TextView) findViewById(R.id.reason_text);
        infor_text = (TextView) findViewById(R.id.infor_text);
        pop_show = (TextView) findViewById(R.id.pop_show);

    }

    @Override
    protected void getExras() {
        orderid = mIntent.getStringExtra("orderid");
        cartid = mIntent.getStringExtra("cartid");
        goodsid = mIntent.getStringExtra("goodsid");
    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        title_text.setText("退款详情");
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 填写数据
     *
     * @param data
     */
    private void setData(RefundDetails data) {
        if ("1".equals(data.getAccountreturn())) {
            tui_outcome.setText("审核中");
            tui_outcome.setTextColor(getResources().getColor(R.color.tuikuan));
            outcome_text.setText("您的申请正在审核，请稍等~~");
            pop_show.setVisibility(View.GONE);
        } else if ("2".equals(data.getAccountreturn())) {
            tui_outcome.setText("同意退款!");
            tui_outcome.setTextColor(getResources().getColor(R.color.title_backgroid));
            outcome_text.setText("您的申请已同意");
            pop_show.setVisibility(View.VISIBLE);
        } else if ("4".equals(data.getAccountreturn())) {
            tui_outcome.setText("退款失败!");
            tui_outcome.setTextColor(getResources().getColor(R.color.jiaoyi_jian));
            outcome_text.setText("您的申请已失败");
            pop_show.setVisibility(View.GONE);
        } else {
            tui_outcome.setText("退款成功");
            tui_outcome.setTextColor(getResources().getColor(R.color.title_backgroid));
            outcome_text.setText("您的退款已完成");
            pop_show.setVisibility(View.GONE);

        }
        //申请原因
        reason_text.setText(data.getReturndemo());
        //申请信息
        if ("1".equals(data.getStatus())) {
            infor_text.setText("申请时间:" + data.getReturntime() + "\n订单号:" + data.getOrdernum() + "\n订单时间:" + data.getTime() + "\n订单状态:待付款");
        } else if ("2".equals(data.getStatus()))
            infor_text.setText("申请时间:" + data.getReturntime() + "\n订单号:" + data.getOrdernum() + "\n订单时间:" + data.getTime() + "\n订单状态:待发货");
        else if ("3".equals(data.getStatus()))
            infor_text.setText("申请时间:" + data.getReturntime() + "\n订单号:" + data.getOrdernum() + "\n订单时间:" + data.getTime() + "\n订单状态:待收货");
        else if ("4".equals(data.getStatus()))
            infor_text.setText("申请时间:" + data.getReturntime() + "\n订单号:" + data.getOrdernum() + "\n订单时间:" + data.getTime() + "\n订单状态:待评价");
        else if ("5".equals(data.getStatus()))
            infor_text.setText("申请时间:" + data.getReturntime() + "\n订单号:" + data.getOrdernum() + "\n订单时间:" + data.getTime() + "\n订单状态:已完成");
        else if ("6".equals(data.getStatus()))
            infor_text.setText("申请时间:" + data.getReturntime() + "\n订单号:" + data.getOrdernum() + "\n订单时间:" + data.getTime() + "\n订单状态:申请退款");
        else if ("7".equals(data.getStatus()))
            infor_text.setText("申请时间:" + data.getReturntime() + "\n订单号:" + data.getOrdernum() + "\n订单时间:" + data.getTime() + "\n订单状态:退款完成");
        else if ("8".equals(data.getStatus()))
            infor_text.setText("申请时间:" + data.getReturntime() + "\n订单号:" + data.getOrdernum() + "\n订单时间:" + data.getTime() + "\n订单状态:已关闭");
        else if ("9".equals(data.getStatus()))
            infor_text.setText("申请时间:" + data.getReturntime() + "\n订单号:" + data.getOrdernum() + "\n订单时间:" + data.getTime() + "\n订单状态:退款失败");
        pop_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showView();
            }
        });
    }
    private class ViewHolder{
        EditText kuaidi_name;
        EditText kuaidi_number;
        TextView yas_pop;
    }
    private void showView()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwind_input_number,null);
        holder = new ViewHolder();
        holder.kuaidi_name = (EditText) view.findViewById(R.id.kuaidi_name);
        holder.kuaidi_number = (EditText) view.findViewById(R.id.kuaidi_number);
        holder.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        //确定
        holder.yas_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String dd_name = holder.kuaidi_name.getText().toString().trim();
                String dd_number = holder.kuaidi_number.getText().toString().trim();
                if (isNull(dd_name))
                {
                    showTextDialog("请填写快递名称");
                    return;
                }
                if (isNull(dd_number))
                {
                    showTextDialog("请填写快递单号");
                    return;
                }
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().deliverySave(token,orderid,cartid,dd_name,dd_number);
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
}
