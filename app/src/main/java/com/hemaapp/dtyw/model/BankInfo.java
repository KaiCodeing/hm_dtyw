package com.hemaapp.dtyw.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 银行卡信息
 */
public class BankInfo  extends XtomObject implements Serializable {
    private String bank_user;//	银行卡用户名
    private String bank_name;//		银行名称
    private String bank_card;//		银行卡号
    private String bank_open;//		开户行地址
    private String bank_id;//银行卡id

    private String id;//银行卡id
    private String name;//银行卡id

    public BankInfo(JSONObject jsonObject) throws DataParseException{
        if(jsonObject != null){
            try {
                bank_id = get(jsonObject, "bank_id");
                bank_user = get(jsonObject, "bank_user");
                bank_name = get(jsonObject, "bank_name");
                bank_card = get(jsonObject, "bank_card");
                bank_open = get(jsonObject, "bank_open");

                id = get(jsonObject, "id");
                name = get(jsonObject, "name");

                log_d(toString());
            } catch (JSONException e) {
                throw new  DataParseException(e);
            }
        }
    }

    public BankInfo(String bank_user, String bank_id, String bank_name,String bank_card, String bank_open) {
        this.bank_user = bank_user;
        this.bank_id = bank_id;
        this.bank_name = bank_name;
        this.bank_card = bank_card;
        this.bank_open = bank_open;
    }

    public String getBank_user() {
        return bank_user;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getBank_card() {
        return bank_card;
    }

    public String getBank_open() {
        return bank_open;
    }

    public String getBank_id() {
        return bank_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BankInfo{" +
                "bank_user='" + bank_user + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", bank_card='" + bank_card + '\'' +
                ", bank_open='" + bank_open + '\'' +
                ", bank_id='" + bank_id + '\'' +
                '}';
    }
}
