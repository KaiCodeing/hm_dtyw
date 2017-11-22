package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/25.
 * 默认地址
 */
public class Defaultadd extends XtomObject implements Serializable {
    private String id;
    private String  clientname;
    private String  tel;
    private String address;
    public Defaultadd(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                clientname = get(jsonObject, "clientname");
                tel = get(jsonObject, "tel");
                address = get(jsonObject, "address");

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Defaultadd{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", clientname='" + clientname + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public String getClientname() {
        return clientname;
    }

    public String getId() {
        return id;
    }

    public String getTel() {
        return tel;
    }
}
