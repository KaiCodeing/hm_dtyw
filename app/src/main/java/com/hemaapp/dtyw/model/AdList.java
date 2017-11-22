package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/12.
 */
public class AdList extends XtomObject implements Serializable {
    private String id;
    private String jump_type;
    private String ad_content;
    private String relative_content;
    private String imgurlbig;
    private String imgurl;
    private String ad_location;
    private String ad_order;
    private String ad_time;
    private String typeurl;
    private String brandname;
    public AdList(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                jump_type = get(jsonObject, "jump_type");
                ad_content = get(jsonObject, "ad_content");

                relative_content = get(jsonObject, "relative_content");
                imgurlbig = get(jsonObject, "imgurlbig");
                imgurl = get(jsonObject, "imgurl");
                ad_location = get(jsonObject, "ad_location");
                ad_order = get(jsonObject, "ad_order");
                ad_time = get(jsonObject, "ad_time");
                typeurl = get(jsonObject, "typeurl");
                brandname = get(jsonObject,"brandname");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "AdList{" +
                "ad_content='" + ad_content + '\'' +
                ", id='" + id + '\'' +
                ", jump_type='" + jump_type + '\'' +
                ", relative_content='" + relative_content + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", ad_location='" + ad_location + '\'' +
                ", ad_order='" + ad_order + '\'' +
                ", ad_time='" + ad_time + '\'' +
                ", typeurl='" + typeurl + '\'' +
                ", brandname='" + brandname + '\'' +
                '}';
    }

    public String getBrandname() {
        return brandname;
    }

    public String getAd_content() {
        return ad_content;
    }

    public String getAd_location() {
        return ad_location;
    }

    public String getAd_order() {
        return ad_order;
    }

    public String getAd_time() {
        return ad_time;
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

    public String getJump_type() {
        return jump_type;
    }

    public String getRelative_content() {
        return relative_content;
    }

    public String getTypeurl() {
        return typeurl;
    }
}
