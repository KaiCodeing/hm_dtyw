package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/17.
 * 规格
 */
public class Value extends XtomObject implements Serializable {
    private String id;
    private String goods_id;
    private String name_id;
    private String name;
    private boolean check;
    public Value(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                goods_id = get(jsonObject, "goods_id");
                name_id = get(jsonObject, "name_id");
                name = get(jsonObject, "name");
                check = false;
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Value{" +
                "goods_id='" + goods_id + '\'' +
                ", id='" + id + '\'' +
                ", name_id='" + name_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getName_id() {
        return name_id;
    }
}
