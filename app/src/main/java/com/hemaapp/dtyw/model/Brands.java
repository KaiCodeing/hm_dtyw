package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/12.
 * 品牌列表
 */
public class Brands extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String imgurl;
    private String imgurlbig;
    private String rec;
    private String orderby;
    private String charindex;
    private boolean check;
    public Brands(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                rec = get(jsonObject, "rec");
                orderby = get(jsonObject, "orderby");
                charindex = get(jsonObject, "charindex");
                check=false;
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Brands{" +
                "charindex='" + charindex + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", rec='" + rec + '\'' +
                ", orderby='" + orderby + '\'' +
                ", check=" + check +
                '}';
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public String getCharindex() {
        return charindex;
    }

    public String getId() {
        return id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getName() {
        return name;
    }

    public String getOrderby() {
        return orderby;
    }

    public String getRec() {
        return rec;
    }
}
