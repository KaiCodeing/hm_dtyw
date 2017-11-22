package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/26.
 * 收货地址列表
 */
public class Shipaddress extends XtomObject implements Serializable {
    private String id;
    private String clientname;
    private String tel;
    private String address;
    private String city;
    private String location;
    private String defaultadd;

    public Shipaddress(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                clientname = get(jsonObject, "clientname");
                tel = get(jsonObject, "tel");
                address = get(jsonObject, "address");
                city = get(jsonObject, "city");
                location = get(jsonObject, "location");
                defaultadd = get(jsonObject, "defaultadd");


                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Shipaddress{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", clientname='" + clientname + '\'' +
                ", tel='" + tel + '\'' +
                ", city='" + city + '\'' +
                ", location='" + location + '\'' +
                ", defaultadd='" + defaultadd + '\'' +
                '}';
    }

    public void setDefaultadd(String defaultadd) {
        this.defaultadd = defaultadd;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getClientname() {
        return clientname;
    }

    public String getDefaultadd() {
        return defaultadd;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getTel() {
        return tel;
    }
}
