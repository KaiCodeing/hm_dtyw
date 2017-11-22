package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/11/3.
 * 银行卡列表
 */
public class Bank extends XtomObject implements Serializable {
    private String id;
    private String name;
    private boolean check;
    public Bank(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                check = false;
                log_d(toString());
            } catch (JSONException e) {
                throw new  DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
