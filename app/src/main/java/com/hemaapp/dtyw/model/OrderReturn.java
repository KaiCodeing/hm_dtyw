package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/11/1.
 */
public class OrderReturn extends XtomObject implements Serializable {
    private String orderid;//订单id
    private String ordernum;//订单编号
    private String shipment;//运费
    private String cartid;//商品所属购物车id
    private String goodsid;//商品id
    private String goodsname;//商品名
    private String goodsnum;//商品数量
    private String goodsprice;//商品总价格
    private String price;//商品单价
    private String refundmoney;//退款总额
    private String accountreturn;//退款状态（仅在退款列表中用到）
    private String goodsimgurl;//商品小图片
    private String goodsimgurlbig;//商品大图片
    private String install;//是否需要安装服务网点
    private String installname;//安装服务网点名字
    private String installaddress;//安装服务网点地址
    private String installphone;//电话
    private String installprice;//安装服务价格
    private String goodspropertyname;
    public OrderReturn(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                orderid = get(jsonObject, "orderid");
                ordernum = get(jsonObject, "ordernum");
                shipment = get(jsonObject, "shipment");
                cartid = get(jsonObject, "cartid");
                goodsid = get(jsonObject, "goodsid");
                goodsname = get(jsonObject, "goodsname");
                goodsnum = get(jsonObject, "goodsnum");
                goodsprice = get(jsonObject, "goodsprice");

                price = get(jsonObject, "price");
                refundmoney = get(jsonObject, "refundmoney");
                accountreturn = get(jsonObject, "accountreturn");
                goodsimgurl = get(jsonObject, "goodsimgurl");
                goodsimgurlbig = get(jsonObject, "goodsimgurlbig");
                install = get(jsonObject, "install");
                installname = get(jsonObject, "installname");
                installaddress = get(jsonObject, "installaddress");
                installphone = get(jsonObject, "installphone");
                installprice = get(jsonObject, "installprice");
                goodspropertyname = get(jsonObject,"goodspropertyname");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "OrderReturn{" +
                "accountreturn='" + accountreturn + '\'' +
                ", orderid='" + orderid + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", shipment='" + shipment + '\'' +
                ", cartid='" + cartid + '\'' +
                ", goodsid='" + goodsid + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", goodsnum='" + goodsnum + '\'' +
                ", goodsprice='" + goodsprice + '\'' +
                ", price='" + price + '\'' +
                ", refundmoney='" + refundmoney + '\'' +
                ", goodsimgurl='" + goodsimgurl + '\'' +
                ", goodsimgurlbig='" + goodsimgurlbig + '\'' +
                ", install='" + install + '\'' +
                ", installname='" + installname + '\'' +
                ", installaddress='" + installaddress + '\'' +
                ", installphone='" + installphone + '\'' +
                ", installprice='" + installprice + '\'' +
                ", goodspropertyname='" + goodspropertyname + '\'' +
                '}';
    }

    public String getGoodspropertyname() {
        return goodspropertyname;
    }

    public String getAccountreturn() {
        return accountreturn;
    }

    public String getCartid() {
        return cartid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public String getGoodsimgurl() {
        return goodsimgurl;
    }

    public String getGoodsimgurlbig() {
        return goodsimgurlbig;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public String getGoodsnum() {
        return goodsnum;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public String getInstall() {
        return install;
    }

    public String getInstalladdress() {
        return installaddress;
    }

    public String getInstallname() {
        return installname;
    }

    public String getInstallphone() {
        return installphone;
    }

    public String getInstallprice() {
        return installprice;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public String getPrice() {
        return price;
    }

    public String getRefundmoney() {
        return refundmoney;
    }

    public String getShipment() {
        return shipment;
    }
}
