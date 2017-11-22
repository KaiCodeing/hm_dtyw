package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/17.
 * 安装
 */

public class Installservice extends XtomObject implements Serializable {
    private String name;
    private String address;
    private String price;
    private String phone;
    private String id;
    private boolean check;
    public Installservice(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                address = get(jsonObject, "seraddress");
                name = get(jsonObject, "name");
                phone = get(jsonObject, "phone");
                check = false;
                price = get(jsonObject, "price");
                id = get(jsonObject, "id");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Installservice{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", phone='" + phone + '\'' +
                ", id='" + id + '\'' +
                ", check=" + check +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getAddress() {
        return address;
    }

    public boolean isCheck() {
        return check;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrice() {
        return price;
    }
}
