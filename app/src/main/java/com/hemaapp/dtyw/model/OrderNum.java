package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/12/1.
 * 订单各个状态的数量
 */
public class OrderNum extends XtomObject implements Serializable {
    private String dfknum;
    private String dfhnum;
    private String dshnum;
    private String dpjnum;
    public OrderNum(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                dfknum = get(jsonObject, "dfknum");
                dfhnum = get(jsonObject, "dfhnum");
                dshnum = get(jsonObject, "dshnum");
                dpjnum = get(jsonObject, "dpjnum");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "OrderNum{" +
                "dfhnum='" + dfhnum + '\'' +
                ", dfknum='" + dfknum + '\'' +
                ", dshnum='" + dshnum + '\'' +
                ", dpjnum='" + dpjnum + '\'' +
                '}';
    }

    public String getDfhnum() {
        return dfhnum;
    }

    public String getDfknum() {
        return dfknum;
    }

    public String getDpjnum() {
        return dpjnum;
    }

    public String getDshnum() {
        return dshnum;
    }
}
