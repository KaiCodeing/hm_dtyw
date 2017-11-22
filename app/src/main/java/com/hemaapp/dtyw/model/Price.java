package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/17.
 * 价格
 */
public class Price extends XtomObject implements Serializable {
    private String id;
    private String name_path;
    private String path;
    private String price;
    private String stock;
    public Price(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name_path = get(jsonObject, "name_path");
                path = get(jsonObject, "path");
                price = get(jsonObject, "price");
                stock = get(jsonObject, "stock");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Price{" +
                "id='" + id + '\'' +
                ", name_path='" + name_path + '\'' +
                ", path='" + path + '\'' +
                ", price='" + price + '\'' +
                ", stock='" + stock + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName_path() {
        return name_path;
    }

    public String getPath() {
        return path;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }
}
