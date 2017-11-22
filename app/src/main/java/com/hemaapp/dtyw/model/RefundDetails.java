package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/11/1.
 * 退款详情
 */
public class RefundDetails extends XtomObject implements Serializable {
    private String accountreturn;//退款状态
    private String returndemo;//退款原因
    private String returntime;//申请退款时间
    private String ordernum;//订单编号
    private String status;//订单状态
    private String time;//下单时间
    public RefundDetails(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                accountreturn = get(jsonObject, "accountreturn");
                returndemo = get(jsonObject, "returndemo");
                returntime = get(jsonObject, "returntime");
                ordernum = get(jsonObject, "ordernum");
                status = get(jsonObject, "status");
                time = get(jsonObject, "time");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getAccountreturn() {
        return accountreturn;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public String getReturndemo() {
        return returndemo;
    }

    public String getReturntime() {
        return returntime;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "RefundDetails{" +
                "accountreturn='" + accountreturn + '\'' +
                ", returndemo='" + returndemo + '\'' +
                ", returntime='" + returntime + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", status='" + status + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
