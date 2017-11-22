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
 * 订单列表
 */
public class OrderList extends XtomObject implements Serializable {
    private String id;
    private String ordernum;
    private String status;
    private String money;
    private String time;
    private String accountreturn;
    private boolean check;
    private ArrayList<GoodsItems> goodsItems;
    public OrderList(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                ordernum = get(jsonObject, "ordernum");
                status = get(jsonObject, "status");
                money = get(jsonObject, "money");
                time = get(jsonObject, "time");
                check = false;
                accountreturn = get(jsonObject, "accountreturn");
                if (!jsonObject.isNull("goodsItems")
                        && !isNull(jsonObject.getString("goodsItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("goodsItems");
                    int size = jsonList.length();
                    goodsItems = new ArrayList<GoodsItems>();
                    for (int i = 0; i < size; i++)
                        goodsItems
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
        return "OrderList{" +
                "accountreturn='" + accountreturn + '\'' +
                ", id='" + id + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", status='" + status + '\'' +
                ", money='" + money + '\'' +
                ", time='" + time + '\'' +
                ", goodsItems=" + goodsItems +
                '}';
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getAccountreturn() {
        return accountreturn;
    }

    public ArrayList<GoodsItems> getGoodsItems() {
        return goodsItems;
    }

    public String getId() {
        return id;
    }

    public String getMoney() {
        return money;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }
}
