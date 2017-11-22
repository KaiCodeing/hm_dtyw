package com.hemaapp.dtyw.model;

import com.hemaapp.hm_FrameWork.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/28.
 * 需求汇总
 */
public class Requirement extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String brand;
    private String property;
    private String memo;
    private String phone;
    private String province;
    private String city;
    private String area;
    private String address;
    private String regdate;
    private String imgurl;
    private String imgurlbig;
    private String cid;
    private ArrayList<Image> imgItems;
    public Requirement(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                cid = get(jsonObject, "cid");
                name = get(jsonObject, "name");
                brand = get(jsonObject, "brand");
                property = get(jsonObject, "property");
                memo = get(jsonObject, "memo");
                phone = get(jsonObject, "phone");
                regdate = get(jsonObject, "regdate");
                province = get(jsonObject, "province");
                city = get(jsonObject, "city");
                area = get(jsonObject, "area");
                address = get(jsonObject, "address");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                if (!jsonObject.isNull("imgItems")
                        && !isNull(jsonObject.getString("imgItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("imgItems");
                    int size = jsonList.length();
                    imgItems = new ArrayList<Image>();
                    for (int i = 0; i < size; i++)
                        imgItems
                                .add(new Image(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getCid() {
        return cid;
    }

    public ArrayList<Image> getImgItems() {
        return imgItems;
    }

    @Override
    public String toString() {
        return "Requirement{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", property='" + property + '\'' +
                ", memo='" + memo + '\'' +
                ", phone='" + phone + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", regdate='" + regdate + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", cid='" + cid + '\'' +
                ", imgItems=" + imgItems +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }

    public String getBrand() {
        return brand;
    }

    public String getCity() {
        return city;
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

    public String getMemo() {
        return memo;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getProperty() {
        return property;
    }

    public String getProvince() {
        return province;
    }

    public String getRegdate() {
        return regdate;
    }
}
