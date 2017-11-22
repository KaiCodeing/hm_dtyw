package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/12.
 * 商品
 */
public class Goods extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String imgurl;
    private String imgurlbig;
    private String price;
    private String salses;
    private String starnum;
    public Goods(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                price = get(jsonObject, "price");
                salses = get(jsonObject, "salses");
                starnum = get(jsonObject, "starnum");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", price='" + price + '\'' +
                ", salses='" + salses + '\'' +
                ", starnum='" + starnum + '\'' +
                '}';
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

    public String getPrice() {
        return price;
    }

    public String getSalses() {
        return salses;
    }

    public String getStarnum() {
        return starnum;
    }
}
