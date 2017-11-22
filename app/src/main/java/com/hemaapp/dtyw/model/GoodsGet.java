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
 * Created by lenovo on 2016/10/17.
 * 商品详情
 */
public class GoodsGet extends XtomObject implements Serializable{
    private String name;
    private String description;
    private String place;
    private String stock;
    private String salses;
    private String shipment;
    private String replycount;
    private String collect;
    private String replytype;
    private ArrayList<Image> imgItems;
    private ArrayList<Price> price;
    private ArrayList<Property> propery;
    private String number;
    private String imgurl;
    private String imgurlbig;
    public GoodsGet(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                name = get(jsonObject, "name");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");

                collect = get(jsonObject, "collect");
                description = get(jsonObject, "description");
                place = get(jsonObject, "place");
                stock = get(jsonObject, "stock");
                salses = get(jsonObject, "salses");
                shipment = get(jsonObject, "shipment");
                replycount = get(jsonObject, "replycount");
                replytype = get(jsonObject, "replytype");
                number = "1";
                if (!jsonObject.isNull("imgItems")
                        && !isNull(jsonObject.getString("imgItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("imgItems");
                    int size = jsonList.length();
                    imgItems = new ArrayList<Image>();
                    for (int i = 0; i < size; i++)
                        imgItems
                                .add(new Image(jsonList.getJSONObject(i)));
                }

                if (!jsonObject.isNull("price")
                        && !isNull(jsonObject.getString("price"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("price");
                    int size = jsonList.length();
                    price = new ArrayList<Price>();
                    for (int i = 0; i < size; i++)
                        price
                                .add(new Price(jsonList.getJSONObject(i)));
                }

                if (!jsonObject.isNull("propery")
                        && !isNull(jsonObject.getString("propery"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("propery");
                    int size = jsonList.length();
                    propery = new ArrayList<Property>();
                    for (int i = 0; i < size; i++)
                        propery
                                .add(new Property(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "GoodsGet{" +
                "collect='" + collect + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", place='" + place + '\'' +
                ", stock='" + stock + '\'' +
                ", salses='" + salses + '\'' +
                ", shipment='" + shipment + '\'' +
                ", replycount='" + replycount + '\'' +
                ", replytype='" + replytype + '\'' +
                ", imgItems=" + imgItems +
                ", price=" + price +
                ", propery=" + propery +
                ", number='" + number + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                '}';
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReplytype() {
        return replytype;
    }

    public String getCollect() {
        return collect;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Image> getImgItems() {
        return imgItems;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public ArrayList<Price> getPrice() {
        return price;
    }

    public ArrayList<Property> getPropery() {
        return propery;
    }

    public String getReplycount() {
        return replycount;
    }

    public String getSalses() {
        return salses;
    }

    public String getShipment() {
        return shipment;
    }

    public String getStock() {
        return stock;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }
}
