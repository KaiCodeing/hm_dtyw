package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/25.
 * 购物车
 */
public class Car extends XtomObject implements Serializable {
    private String id;//购物车id
    private String keyid;//商品id
    private String buycount;//数量
    private String goodsname;//商品名
    private String imgurl;//图片
    private String imgurlbig;//大图片
    private String propertyname;//规格
    private String totalprice;//每件商品总价
    private String price;//每件商品单价
    private String installid;//安装服务网点id
    private String installprice;//安装服务网点价格
    private String name;//安装服务网点名称
    private String seraddress;//安装服务网点地点
    private String phone;//安装服务网点电话
    private String city;//安装服务网点所在城市
    private String shipment;//运费
    private boolean check;
    public Car(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                keyid = get(jsonObject, "keyid");
                buycount = get(jsonObject, "buycount");
                goodsname = get(jsonObject, "goodsname");

                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                propertyname = get(jsonObject, "propertyname");
                totalprice = get(jsonObject, "totalprice");
                price = get(jsonObject, "price");
                installid = get(jsonObject, "installid");
                installprice = get(jsonObject, "installprice");
                seraddress = get(jsonObject, "seraddress");
                phone = get(jsonObject, "phone");
                check = false;
                city = get(jsonObject, "city");
                shipment = get(jsonObject, "shipment");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Car{" +
                "buycount='" + buycount + '\'' +
                ", id='" + id + '\'' +
                ", keyid='" + keyid + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", propertyname='" + propertyname + '\'' +
                ", totalprice='" + totalprice + '\'' +
                ", price='" + price + '\'' +
                ", installid='" + installid + '\'' +
                ", installprice='" + installprice + '\'' +
                ", name='" + name + '\'' +
                ", seraddress='" + seraddress + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", shipment='" + shipment + '\'' +
                '}';
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public String getBuycount() {
        return buycount;
    }

    public String getCity() {
        return city;
    }

    public String getGoodsname() {
        return goodsname;
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

    public String getInstallid() {
        return installid;
    }

    public String getInstallprice() {
        return installprice;
    }

    public String getKeyid() {
        return keyid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrice() {
        return price;
    }

    public String getPropertyname() {
        return propertyname;
    }

    public String getSeraddress() {
        return seraddress;
    }

    public String getShipment() {
        return shipment;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setBuycount(String buycount) {
        this.buycount = buycount;
    }
}
