package com.hemaapp.dtyw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/19.
 */
public class BrandsTw extends XtomObject implements Serializable {
    private String charindex;
    private ArrayList<Brands> brands;
    public BrandsTw(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                charindex = get(jsonObject, "charindex");
                if (!jsonObject.isNull("brands")
                        && !isNull(jsonObject.getString("brands"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("brands");
                    int size = jsonList.length();
                    brands = new ArrayList<Brands>();
                    for (int i = 0; i < size; i++)
                        brands
                                .add(new Brands(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "BrandsTw{" +
                "brands=" + brands +
                ", charindex='" + charindex + '\'' +
                '}';
    }

    public ArrayList<Brands> getBrands() {
        return brands;
    }

    public String getCharindex() {
        return charindex;
    }
}
