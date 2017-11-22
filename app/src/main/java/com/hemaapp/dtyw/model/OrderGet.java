package com.hemaapp.dtyw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/31.
 */
public class OrderGet extends XtomObject implements Serializable {
    private String id;//订单id
    private String ordernum;//订单编号
    private String status;//订单状态
    private String money;//订单总付款
    private String time;//下单时间
    private String invoice;//发票类型
    private String express;//快递公司
    private String expressnum;//物流单号
    private String paytype;//订单支付方式
    private String shipment;//订单运费
    private String invoiceheader;//普通发票抬头
    private String invoiceitem;//普通发票明细
    private String company;//专用发票公司
    private String companyaddress;//专用发票地址
    private String conmpanytel;//专用发票电话
    private String bank;//专用发票开户银行
    private String taxnum;//专用发票税号
    private String invoicedemo;//发票备注
    private String addressid;//收货地址id
    private String clientname;//收货人姓名
    private String tel;//收货人电话
    private String address;//收货地址
    private String banknum;//账号
    private ArrayList<GoodsItems> goodsItem;//订单商品详情
    public OrderGet(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                address = get(jsonObject, "address");
                id = get(jsonObject, "id");
                ordernum = get(jsonObject, "ordernum");
                status = get(jsonObject, "status");
                money = get(jsonObject, "money");
                time = get(jsonObject, "time");
                invoice = get(jsonObject, "invoice");
                express = get(jsonObject, "express");
                expressnum = get(jsonObject, "expressnum");
                paytype = get(jsonObject, "paytype");
                shipment = get(jsonObject, "shipment");
                invoiceheader = get(jsonObject, "invoiceheader");
                invoiceitem = get(jsonObject, "invoiceitem");
                company = get(jsonObject, "company");
                companyaddress = get(jsonObject, "companyaddress");
                conmpanytel = get(jsonObject, "conmpanytel");
                bank = get(jsonObject, "bank");
                taxnum = get(jsonObject, "taxnum");
                invoicedemo = get(jsonObject, "invoicedemo");
                addressid = get(jsonObject, "addressid");
                clientname = get(jsonObject, "clientname");
                tel = get(jsonObject, "tel");
                banknum = get(jsonObject,"banknum");
                if (!jsonObject.isNull("goodsItem")
                        && !isNull(jsonObject.getString("goodsItem"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("goodsItem");
                    int size = jsonList.length();
                    goodsItem = new ArrayList<GoodsItems>();
                    for (int i = 0; i < size; i++)
                        goodsItem
                                .add(new GoodsItems(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "OrderGet{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", status='" + status + '\'' +
                ", money='" + money + '\'' +
                ", time='" + time + '\'' +
                ", invoice='" + invoice + '\'' +
                ", express='" + express + '\'' +
                ", expressnum='" + expressnum + '\'' +
                ", paytype='" + paytype + '\'' +
                ", shipment='" + shipment + '\'' +
                ", invoiceheader='" + invoiceheader + '\'' +
                ", invoiceitem='" + invoiceitem + '\'' +
                ", company='" + company + '\'' +
                ", companyaddress='" + companyaddress + '\'' +
                ", conmpanytel='" + conmpanytel + '\'' +
                ", bank='" + bank + '\'' +
                ", taxnum='" + taxnum + '\'' +
                ", invoicedemo='" + invoicedemo + '\'' +
                ", addressid='" + addressid + '\'' +
                ", clientname='" + clientname + '\'' +
                ", tel='" + tel + '\'' +
                ", banknum='" + banknum + '\'' +
                ", goodsItem=" + goodsItem +
                '}';
    }

    public String getBanknum() {
        return banknum;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressid() {
        return addressid;
    }

    public String getBank() {
        return bank;
    }

    public String getClientname() {
        return clientname;
    }

    public String getCompany() {
        return company;
    }

    public String getCompanyaddress() {
        return companyaddress;
    }

    public String getConmpanytel() {
        return conmpanytel;
    }

    public String getExpress() {
        return express;
    }

    public String getExpressnum() {
        return expressnum;
    }

    public ArrayList<GoodsItems> getGoodsItem() {
        return goodsItem;
    }

    public String getId() {
        return id;
    }


    public String getInvoice() {
        return invoice;
    }

    public String getInvoicedemo() {
        return invoicedemo;
    }

    public String getInvoiceheader() {
        return invoiceheader;
    }

    public String getInvoiceitem() {
        return invoiceitem;
    }

    public String getMoney() {
        return money;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public String getPaytype() {
        return paytype;
    }

    public String getShipment() {
        return shipment;
    }

    public String getStatus() {
        return status;
    }

    public String getTaxnum() {
        return taxnum;
    }

    public String getTel() {
        return tel;
    }

    public String getTime() {
        return time;
    }
}
