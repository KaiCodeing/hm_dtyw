package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/20.
 * 价格区间
 */
public class Pricerange extends XtomObject implements Serializable {
    private String id;
    private String description;
    private boolean check;
    public Pricerange(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                check = false;
                description = get(jsonObject, "description");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Pricerange{" +
                "check=" + check +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCheck() {
        return check;
    }
}
