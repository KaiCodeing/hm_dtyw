package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/29.
 */
public class PayList extends XtomObject implements Serializable {
    private String client_id;
    private String keytype;
    private String paytype;
    private String out_trade_no;
    private String trade_no;
    private String total_fee;
    private String payflag;
    private String regdate;
    private String amount;//金额
    private String content;//描述

    public PayList(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                client_id = get(jsonObject, "client_id");
                keytype = get(jsonObject, "keytype");
                paytype = get(jsonObject, "paytype");
                out_trade_no = get(jsonObject, "out_trade_no");
                trade_no = get(jsonObject, "trade_no");
                total_fee = get(jsonObject, "total_fee");
                payflag = get(jsonObject, "payflag");
                regdate = get(jsonObject, "regdate");
                amount = get(jsonObject, "amount");
                content = get(jsonObject, "content");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "PayList{" +
                "amount='" + amount + '\'' +
                ", client_id='" + client_id + '\'' +
                ", keytype='" + keytype + '\'' +
                ", paytype='" + paytype + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", trade_no='" + trade_no + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", payflag='" + payflag + '\'' +
                ", regdate='" + regdate + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getAmount() {
        return amount;
    }

    public String getContent() {
        return content;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public String getPayflag() {
        return payflag;
    }

    public String getPaytype() {
        return paytype;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public String getTrade_no() {
        return trade_no;
    }
}
