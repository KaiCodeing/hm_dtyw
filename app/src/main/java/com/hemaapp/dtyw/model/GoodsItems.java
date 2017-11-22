package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/31.
 * 订单列表里的子列表
 */
public class GoodsItems extends XtomObject implements Serializable {
    private String cartid;
    private String goodsid;
    private String goodsname;
    private String goodsnum;
    private String goodsimgurl;
    private String goodsimgurlbig;
    private String goodsprice;
    private String price;
    private String goodspropertyid;
    private String goodspropertyname;
    private String goodsshipment;
    private String install;//是否需要安装服务网点
    private String installserviceid;//安装服务网点id
    private String installname;//安装服务网点名称
    private String installaddress;//安装服务网点地址
    private String installphone;//安装服务网点电话
    private String installprice;//安装服务网点价格
    private String installcity;//安装服务网点城市
    private String reply;//是否评论	0：否,可以退款 1：是 ，不能退款了
    private String accountreturn;//0：否 1：退款申请中 2:同意退款 3：已退款 4:退款失败
    public GoodsItems(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                cartid = get(jsonObject, "cartid");
                goodsid = get(jsonObject, "goodsid");
                goodsname = get(jsonObject, "goodsname");
                goodsnum = get(jsonObject, "goodsnum");
                goodsimgurl = get(jsonObject, "goodsimgurl");
                goodsimgurlbig = get(jsonObject, "goodsimgurlbig");
                accountreturn = get(jsonObject, "accountreturn");
                goodsprice = get(jsonObject, "goodsprice");
                price = get(jsonObject, "price");
                goodspropertyid = get(jsonObject, "goodspropertyid");
                goodspropertyname = get(jsonObject, "goodspropertyname");
                goodsshipment = get(jsonObject, "goodsshipment");
                install = get(jsonObject, "install");
                installserviceid = get(jsonObject, "installserviceid");
                installname = get(jsonObject, "installname");
                installaddress = get(jsonObject, "installaddress");
                reply = get(jsonObject,"reply");
                installphone = get(jsonObject, "installphone");
                installprice = get(jsonObject, "installprice");
                installcity = get(jsonObject, "installcity");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "GoodsItems{" +
                "accountreturn='" + accountreturn + '\'' +
                ", cartid='" + cartid + '\'' +
                ", goodsid='" + goodsid + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", goodsnum='" + goodsnum + '\'' +
                ", goodsimgurl='" + goodsimgurl + '\'' +
                ", goodsimgurlbig='" + goodsimgurlbig + '\'' +
                ", goodsprice='" + goodsprice + '\'' +
                ", price='" + price + '\'' +
                ", goodspropertyid='" + goodspropertyid + '\'' +
                ", goodspropertyname='" + goodspropertyname + '\'' +
                ", goodsshipment='" + goodsshipment + '\'' +
                ", install='" + install + '\'' +
                ", installserviceid='" + installserviceid + '\'' +
                ", installname='" + installname + '\'' +
                ", installaddress='" + installaddress + '\'' +
                ", installphone='" + installphone + '\'' +
                ", installprice='" + installprice + '\'' +
                ", installcity='" + installcity + '\'' +
                ", reply='" + reply + '\'' +
                '}';
    }

    public String getAccountreturn() {
        return accountreturn;
    }

    public String getReply() {
        return reply;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public String getGoodspropertyid() {
        return goodspropertyid;
    }

    public String getGoodspropertyname() {
        return goodspropertyname;
    }

    public String getGoodsshipment() {
        return goodsshipment;
    }

    public String getPrice() {
        return price;
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
    public String getInstall() {
        return install;
    }

    public String getInstalladdress() {
        return installaddress;
    }

    public String getInstallcity() {
        return installcity;
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

    public String getInstallserviceid() {
        return installserviceid;
    }

}
